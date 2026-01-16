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
import org.springframework.web.bind.annotation.*;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;

@RestController
@RequestMapping("/api/compare")
public class CompareApiController {

    private static final String QUOTE_JAVA_BYTES = "QUOTE_JAVA_BYTES";
    private static final String QUOTE_FORY_BYTES = "QUOTE_FORY_BYTES";
    private static final String QUOTE_OBJECT = "QUOTE_OBJECT";

    @Autowired
    private GenericForyCodec genericForyCodec;

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
        session.setAttribute(QUOTE_OBJECT, obj);
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

        Object obj = session.getAttribute(QUOTE_OBJECT);
        byte[] storedData = (byte[]) session.getAttribute("java".equals(mode) ? QUOTE_JAVA_BYTES : QUOTE_FORY_BYTES);

        if (obj == null || storedData == null) {
            throw new RuntimeException("No data in session. Please 'Store' first.");
        }

        int warmup = request.resolvedWarmup();
        int iterations = request.resolvedIterations();

        // Warmup phase (do not record)
        for (int i = 0; i < warmup; i++) {
            if ("serialize".equals(type)) {
                if ("java".equals(mode)) {
                    JavaSer.serialize(obj);
                } else {
                    genericForyCodec.serialize(obj);
                }
            } else {
                if ("java".equals(mode)) {
                    JavaSer.deserialize(storedData);
                } else {
                    genericForyCodec.deserialize(storedData);
                }
            }
        }

        // Measure GC and Memory before
        System.gc(); // Suggest GC to have a cleaner baseline
        Runtime runtime = Runtime.getRuntime();
        long memBefore = runtime.totalMemory() - runtime.freeMemory();
        long gcCountBefore = getGcCount();
        long gcTimeBefore = getGcTime();

        // Measurement phase
        List<Long> times = new ArrayList<>(iterations);
        List<Double> msSamples = new ArrayList<>(iterations);

        // payloadBytes:
        // - deserialize => size of stored session bytes
        // - serialize => size of bytes produced by the serializer (measured)
        int payloadBytes = storedData.length;
        byte[] lastSerialized = null;

        long totalMeasureStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();

            if ("serialize".equals(type)) {
                if ("java".equals(mode)) {
                    lastSerialized = JavaSer.serialize(obj);
                } else {
                    lastSerialized = genericForyCodec.serialize(obj);
                }
            } else {
                if ("java".equals(mode)) {
                    JavaSer.deserialize(storedData);
                } else {
                    genericForyCodec.deserialize(storedData);
                }
            }

            long end = System.nanoTime();
            long duration = end - start;

            times.add(duration);
            msSamples.add(nanosToMs(duration));
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
                .build();
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
