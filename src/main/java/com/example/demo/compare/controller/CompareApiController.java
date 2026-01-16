package com.example.demo.compare.controller;

import com.example.demo.compare.domain.Quote;
import com.example.demo.compare.domain.QuoteFactory;
import com.example.demo.compare.dto.BenchRequest;
import com.example.demo.compare.dto.BenchResponse;
import com.example.demo.compare.dto.LoadResponse;
import com.example.demo.compare.dto.StoreResponse;
import com.example.demo.compare.serialization.JavaSer;
import com.example.demo.compare.serialization.QuoteCodec;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/compare")
public class CompareApiController {

    private static final String QUOTE_JAVA_BYTES = "QUOTE_JAVA_BYTES";
    private static final String QUOTE_FORY_BYTES = "QUOTE_FORY_BYTES";

    @Autowired
    private QuoteCodec quoteCodec;

    @PostMapping("/store")
    public StoreResponse store(@RequestParam(defaultValue = "100") int sizeKb, HttpSession session) throws Exception {
        Quote quote = QuoteFactory.createLargeQuote(sizeKb);

        byte[] javaBytes = JavaSer.serialize(quote);
        byte[] foryBytes = quoteCodec.serialize(quote);

        session.setAttribute(QUOTE_JAVA_BYTES, javaBytes);
        session.setAttribute(QUOTE_FORY_BYTES, foryBytes);

        return new StoreResponse(session.getId(), sizeKb, javaBytes.length, foryBytes.length);
    }

    @GetMapping("/load/java")
    public LoadResponse loadJava(HttpSession session) throws Exception {
        byte[] data = (byte[]) session.getAttribute(QUOTE_JAVA_BYTES);
        if (data == null)
            throw new RuntimeException("No Java bytes in session");

        long start = System.nanoTime();
        Quote quote = (Quote) JavaSer.deserialize(data);
        long end = System.nanoTime();

        long duration = end - start;
        return new LoadResponse(quote, duration, duration / 1_000_000.0);
    }

    @GetMapping("/load/fory")
    public LoadResponse loadFory(HttpSession session) throws Exception {
        byte[] data = (byte[]) session.getAttribute(QUOTE_FORY_BYTES);
        if (data == null)
            throw new RuntimeException("No Fory bytes in session");

        long start = System.nanoTime();
        Quote quote = quoteCodec.deserialize(data);
        long end = System.nanoTime();

        long duration = end - start;
        return new LoadResponse(quote, duration, duration / 1_000_000.0);
    }

    @PostMapping("/bench")
    public BenchResponse bench(@RequestBody BenchRequest request, HttpSession session) throws Exception {
        String attr = "java".equals(request.getMode()) ? QUOTE_JAVA_BYTES : QUOTE_FORY_BYTES;
        byte[] data = (byte[]) session.getAttribute(attr);
        if (data == null)
            throw new RuntimeException("No data in session for mode: " + request.getMode());

        List<Long> times = new ArrayList<>();
        int iterations = request.getIterations();

        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if ("java".equals(request.getMode())) {
                JavaSer.deserialize(data);
            } else {
                quoteCodec.deserialize(data);
            }
            times.add(System.nanoTime() - start);
        }

        Collections.sort(times);
        double avg = times.stream().mapToLong(Long::longValue).average().orElse(0) / 1_000_000.0;
        double min = times.get(0) / 1_000_000.0;
        double max = times.get(times.size() - 1) / 1_000_000.0;
        double p95 = times.get((int) (times.size() * 0.95)) / 1_000_000.0;

        return BenchResponse.builder()
                .avgMs(avg)
                .minMs(min)
                .maxMs(max)
                .p95Ms(p95)
                .iterations(iterations)
                .payloadBytes(data.length)
                .mode(request.getMode())
                .build();
    }
}
