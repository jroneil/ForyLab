package com.example.demo.compare.dto;

import lombok.Data;

@Data
public class BenchRequest {
    private int iterations;
    private int warmup; // Iterations for JIT warm-up
    private String mode; // "java" or "fory"
    private String type; // "serialize" or "deserialize"
}
