package com.example.demo.compare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BenchResponse {
    private double avgMs;
    private double p95Ms;
    private double minMs;
    private double maxMs;
    private int iterations;
    private int payloadBytes;
    private String mode;
}
