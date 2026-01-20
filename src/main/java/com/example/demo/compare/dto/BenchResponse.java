package com.example.demo.compare.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BenchResponse {

    // ===== ORIGINAL FIELDS (UNCHANGED) =====
    private double avgMs;
    private double p95Ms;
    private double minMs;
    private double maxMs;
    private double throughput; // ops/sec
    private int iterations;
    private int warmup;
    private int payloadBytes;
    private String mode;
    private String type;
    private boolean circular;
    private List<Double> samples; // for histogram

    // ===== NEW (ADDITIVE ONLY) =====
    private double p50Ms; // median
    private double p99Ms; // tail latency
    private double stddevMs; // latency stability

    // JVM Impact (v3.2)
    private long memoryBeforeBytes;
    private long memoryAfterBytes;
    private long memoryDeltaBytes;
    private long gcCollectionsDelta;
    private long gcTimeMsDelta;
    private String objectType;

    // E2E I/O Breakdown (v5.0)
    private double serializeOnlyMs;
    private double ioOnlyMs;
    private String backend; // memory, jdbc, redis
}
