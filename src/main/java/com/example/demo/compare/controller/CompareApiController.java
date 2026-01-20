package com.example.demo.compare.controller;

import com.example.demo.compare.domain.*;
import com.example.demo.compare.dto.BenchRequest;
import com.example.demo.compare.dto.BenchResponse;
import com.example.demo.compare.dto.LoadResponse;
import com.example.demo.compare.dto.StoreResponse;
import com.example.demo.compare.serialization.GenericForyCodec;
import com.example.demo.compare.serialization.JavaSer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/compare")
public class CompareApiController {

    private static final String QUOTE_JAVA_BYTES = "QUOTE_JAVA_BYTES";
    private static final String QUOTE_FORY_BYTES = "QUOTE_FORY_BYTES";
    private static final String QUOTE_SIZE_KB = "QUOTE_SIZE_KB";
    private static final String QUOTE_CIRCULAR = "QUOTE_CIRCULAR";

    @Autowired
    private GenericForyCodec genericForyCodec;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    private final Map<String, byte[]> inMemoryStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        if (jdbcTemplate != null) {
            try {
                jdbcTemplate
                        .execute("CREATE TABLE IF NOT EXISTS FORY_BENCH_STORE (id VARCHAR(50) PRIMARY KEY, data BLOB)");
                System.out.println("✅ JDBC Benchmark table initialized.");
            } catch (Exception e) {
                // Might fail on some DBs if syntax is different, but for H2/SQLServer it's
                // usually fine
                System.err.println("❌ Could not create FORY_BENCH_STORE table: " + e.getMessage());
            }
        }
        if (redisConnectionFactory != null) {
            System.out.println("✅ Redis Connection Factory detected.");
        } else {
            System.err.println("⚠️ Redis Connection Factory NOT found. Redis benchmarks will be skipped.");
        }
    }

    @PostMapping("/store")
    public StoreResponse store(@RequestParam(defaultValue = "100") int sizeKb,
            @RequestParam(defaultValue = "false") boolean circular,
            @RequestParam(defaultValue = "quote") String objectType,
            HttpSession session) throws Exception {

        Object obj;
        if ("policy".equalsIgnoreCase(objectType)) {
            obj = PolicyFactory.createPolicy(sizeKb, circular);
        } else if ("collections".equalsIgnoreCase(objectType)) {
            obj = CollectionsFactory.createCollectionsBlob(sizeKb, circular);
        } else {
            obj = QuoteFactory.createLargeQuote(sizeKb, circular);
        }

        byte[] javaBytes = JavaSer.serialize(obj);
        byte[] foryBytes = genericForyCodec.serialize(obj);

        session.setAttribute(QUOTE_JAVA_BYTES, javaBytes);
        session.setAttribute(QUOTE_FORY_BYTES, foryBytes);
        session.setAttribute(QUOTE_SIZE_KB, sizeKb);
        session.setAttribute(QUOTE_CIRCULAR, circular);
        session.setAttribute("OBJECT_TYPE", objectType);
        session.setAttribute("OBJECT_CLASS", obj.getClass().getSimpleName());

        Map<String, Integer> summary = new HashMap<>();
        if (obj instanceof Quote) {
            Quote q = (Quote) obj;
            summary.put("drivers", q.getDrivers().size());
            summary.put("coverages", q.getCoverages().size());
            summary.put("vehicles", q.getVehicles().size());
        } else if (obj instanceof InsurancePolicy) {
            InsurancePolicy p = (InsurancePolicy) obj;
            summary.put("coverages", p.getCoverages().size());
            summary.put("endorsements", p.getEndorsements().size());
            summary.put("attributes", p.getAttributes().size());
        } else if (obj instanceof CollectionsBlob) {
            CollectionsBlob b = (CollectionsBlob) obj;
            summary.put("items", b.getItems().size());
            summary.put("map_entries", b.getMap().size());
            summary.put("strings", b.getStrings().size());
        }

        return new StoreResponse(session.getId(), sizeKb, javaBytes.length, foryBytes.length, objectType,
                obj.getClass().getSimpleName(), summary);
    }

    @GetMapping("/load/java")
    public LoadResponse loadJava(HttpSession session) throws Exception {
        byte[] data = (byte[]) session.getAttribute(QUOTE_JAVA_BYTES);
        if (data == null)
            throw new RuntimeException("No Java bytes in session");

        long start = System.nanoTime();
        Object obj = JavaSer.deserialize(data);
        long end = System.nanoTime();

        long duration = end - start;
        return new LoadResponse(obj, duration, nanosToMs(duration));
    }

    @GetMapping("/load/fory")
    public LoadResponse loadFory(HttpSession session) throws Exception {
        byte[] data = (byte[]) session.getAttribute(QUOTE_FORY_BYTES);
        if (data == null)
            throw new RuntimeException("No Fory bytes in session");

        long start = System.nanoTime();
        Object obj = genericForyCodec.deserialize(data);
        long end = System.nanoTime();

        long duration = end - start;
        return new LoadResponse(obj, duration, nanosToMs(duration));
    }

    @PostMapping("/bench")
    public BenchResponse bench(@RequestBody BenchRequest request, HttpSession session) throws Exception {
        String mode = request.resolvedMode();
        String type = request.resolvedType();

        if (!"java".equals(mode) && !"fory".equals(mode)) {
            throw new IllegalArgumentException("mode must be 'java' or 'fory'");
        }
        if (!"serialize".equals(type) && !"deserialize".equals(type)) {
            throw new IllegalArgumentException("type must be 'serialize' or 'deserialize'");
        }

        byte[] storedData = (byte[]) session.getAttribute("java".equals(mode) ? QUOTE_JAVA_BYTES : QUOTE_FORY_BYTES);
        Integer sizeKb = (Integer) session.getAttribute(QUOTE_SIZE_KB);
        Boolean circular = (Boolean) session.getAttribute(QUOTE_CIRCULAR);
        String objectType = (String) session.getAttribute("OBJECT_TYPE");

        if (storedData == null || sizeKb == null || circular == null) {
            throw new RuntimeException("No data in session. Please 'Store' first.");
        }

        String backend = request.getBackend(); // memory, jdbc, redis

        // Prime the backend for deserialization benchmarks if needed
        if ("deserialize".equals(type) && backend != null) {
            performStore(backend, storedData);
        }

        // Regenerate object for serialization benchmark if needed
        Object obj = null;
        if ("serialize".equals(type)) {
            obj = createObjectByType(objectType, sizeKb, circular);
        }

        int warmup = request.resolvedWarmup();
        int iterations = request.resolvedIterations();

        // Warmup phase
        for (int i = 0; i < warmup; i++) {
            if ("serialize".equals(type)) {
                byte[] bytes = "java".equals(mode) ? JavaSer.serialize(obj) : genericForyCodec.serialize(obj);
                if (backend != null) {
                    performStore(backend, bytes);
                }
            } else {
                byte[] dataToUse = storedData;
                if (backend != null) {
                    dataToUse = performLoad(backend);
                }
                // Skip if no data available (backend not primed yet)
                if (dataToUse != null && dataToUse.length > 0) {
                    if ("java".equals(mode)) {
                        JavaSer.deserialize(dataToUse);
                    } else {
                        genericForyCodec.deserialize(dataToUse);
                    }
                }
            }
        }

        // Measure GC and Memory before
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long memBefore = runtime.totalMemory() - runtime.freeMemory();
        long gcCountBefore = getGcCount();
        long gcTimeBefore = getGcTime();

        // Measurement phase
        List<Long> times = new ArrayList<>(iterations);
        List<Double> msSamples = new ArrayList<>(iterations);
        long totalSerializeNanos = 0;
        long totalIoNanos = 0;

        int payloadBytes = storedData.length;
        byte[] lastSerialized = null;

        long totalMeasureStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            long serNanos = 0;
            long ioNanos = 0;

            if ("serialize".equals(type)) {
                long s1 = System.nanoTime();
                byte[] bytes = "java".equals(mode) ? JavaSer.serialize(obj) : genericForyCodec.serialize(obj);
                long s2 = System.nanoTime();
                serNanos = s2 - s1;
                lastSerialized = bytes;

                if (backend != null) {
                    long i1 = System.nanoTime();
                    performStore(backend, bytes);
                    long i2 = System.nanoTime();
                    ioNanos = i2 - i1;
                }
            } else {
                byte[] dataToUse = storedData;
                if (backend != null) {
                    long i1 = System.nanoTime();
                    dataToUse = performLoad(backend);
                    long i2 = System.nanoTime();
                    ioNanos = i2 - i1;
                }

                // Skip if no data available
                if (dataToUse != null && dataToUse.length > 0) {
                    long s1 = System.nanoTime();
                    if ("java".equals(mode)) {
                        JavaSer.deserialize(dataToUse);
                    } else {
                        genericForyCodec.deserialize(dataToUse);
                    }
                    long s2 = System.nanoTime();
                    serNanos = s2 - s1;
                }
            }

            long end = System.nanoTime();
            long duration = end - start;

            times.add(duration);
            msSamples.add(nanosToMs(duration));
            totalSerializeNanos += serNanos;
            totalIoNanos += ioNanos;
        }
        long totalMeasureEnd = System.nanoTime();

        // Measure GC and Memory after
        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        long gcCountAfter = getGcCount();
        long gcTimeAfter = getGcTime();

        double totalMeasureSecs = (totalMeasureEnd - totalMeasureStart) / 1_000_000_000.0;
        double throughput = iterations / Math.max(totalMeasureSecs, 1e-9);

        if ("serialize".equals(type) && lastSerialized != null) {
            payloadBytes = lastSerialized.length;
        }

        Collections.sort(times);

        double avg = nanosToMs((long) times.stream().mapToLong(Long::longValue).average().orElse(0));
        double min = nanosToMs(times.get(0));
        double max = nanosToMs(times.get(times.size() - 1));

        double p50 = nanosToMs(percentileSorted(times, 0.50));
        double p95 = nanosToMs(percentileSorted(times, 0.95));
        double p99 = nanosToMs(percentileSorted(times, 0.99));

        double stddev = stddevMs(times);

        return BenchResponse.builder()
                .avgMs(avg)
                .minMs(min)
                .maxMs(max)
                .p50Ms(p50)
                .p95Ms(p95)
                .p99Ms(p99)
                .stddevMs(stddev)
                .throughput(throughput)
                .iterations(iterations)
                .warmup(warmup)
                .payloadBytes(payloadBytes) // ✅ correct for serialize + deserialize
                .mode(mode)
                .type(type)
                // Note: this flag doesn't affect bench unless you regenerate quote here.
                // Keep it for compatibility with your UI.
                .circular(request.isCircular())
                .samples(msSamples)
                .memoryBeforeBytes(memBefore)
                .memoryAfterBytes(memAfter)
                .memoryDeltaBytes(memAfter - memBefore)
                .gcCollectionsDelta(gcCountAfter - gcCountBefore)
                .gcTimeMsDelta(gcTimeAfter - gcTimeBefore)
                .objectType((String) session.getAttribute("OBJECT_TYPE"))
                .serializeOnlyMs(nanosToMs(totalSerializeNanos / iterations))
                .ioOnlyMs(nanosToMs(totalIoNanos / iterations))
                .backend(backend)
                .build();
    }

    private void performStore(String backend, byte[] data) {
        if ("memory".equalsIgnoreCase(backend)) {
            inMemoryStore.put("bench-key", data);
        } else if ("jdbc".equalsIgnoreCase(backend)) {
            if (jdbcTemplate != null) {
                jdbcTemplate.update("MERGE INTO FORY_BENCH_STORE (id, data) KEY(id) VALUES (?, ?)", "bench-key", data);
            }
        } else if ("redis".equalsIgnoreCase(backend)) {
            if (redisConnectionFactory != null) {
                try (RedisConnection conn = redisConnectionFactory.getConnection()) {
                    conn.set("fory-bench-key".getBytes(), data);
                } catch (Exception e) {
                    System.err.println("Redis Store Error: " + e.getMessage());
                }
            }
        }
    }

    private byte[] performLoad(String backend) {
        if ("memory".equalsIgnoreCase(backend)) {
            byte[] data = inMemoryStore.get("bench-key");
            return data != null ? data : new byte[0];
        } else if ("jdbc".equalsIgnoreCase(backend)) {
            if (jdbcTemplate != null) {
                try {
                    byte[] data = jdbcTemplate.queryForObject("SELECT data FROM FORY_BENCH_STORE WHERE id = ?",
                            byte[].class, "bench-key");
                    return data != null ? data : new byte[0];
                } catch (Exception e) {
                    // Row might not exist yet
                    return new byte[0];
                }
            }
        } else if ("redis".equalsIgnoreCase(backend)) {
            if (redisConnectionFactory != null) {
                try (RedisConnection conn = redisConnectionFactory.getConnection()) {
                    byte[] data = conn.get("fory-bench-key".getBytes());
                    return data != null ? data : new byte[0];
                } catch (Exception e) {
                    System.err.println("Redis Load Error: " + e.getMessage());
                    return new byte[0];
                }
            }
        }
        return new byte[0];
    }

    private Object createObjectByType(String objectType, int sizeKb, boolean circular) {
        if ("policy".equalsIgnoreCase(objectType)) {
            return PolicyFactory.createPolicy(sizeKb, circular);
        } else if ("collections".equalsIgnoreCase(objectType)) {
            return CollectionsFactory.createCollectionsBlob(sizeKb, circular);
        } else {
            return QuoteFactory.createLargeQuote(sizeKb, circular);
        }
    }

    private static double nanosToMs(long nanos) {
        return nanos / 1_000_000.0;
    }

    /** p in [0..1], e.g. 0.95. Uses nearest-rank with clamp. */
    private static long percentileSorted(List<Long> sortedNanos, double p) {
        if (sortedNanos.isEmpty())
            return 0L;
        if (p <= 0)
            return sortedNanos.get(0);
        if (p >= 1)
            return sortedNanos.get(sortedNanos.size() - 1);

        int n = sortedNanos.size();
        int rank = (int) Math.ceil(p * n); // 1..n
        int idx = Math.min(Math.max(rank - 1, 0), n - 1);
        return sortedNanos.get(idx);
    }

    private static double stddevMs(List<Long> nanos) {
        int n = nanos.size();
        if (n < 2)
            return 0.0;

        double mean = nanos.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double var = 0.0;

        for (long x : nanos) {
            double d = x - mean;
            var += d * d;
        }
        var /= (n - 1); // sample variance
        return Math.sqrt(var) / 1_000_000.0;
    }

    private long getGcCount() {
        return ManagementFactory.getGarbageCollectorMXBeans().stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionCount)
                .filter(c -> c != -1)
                .sum();
    }

    private long getGcTime() {
        return ManagementFactory.getGarbageCollectorMXBeans().stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionTime)
                .filter(t -> t != -1)
                .sum();
    }
}
