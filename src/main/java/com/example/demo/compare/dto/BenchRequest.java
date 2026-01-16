package com.example.demo.compare.dto;

import lombok.Data;

@Data
public class BenchRequest {
    private int iterations;
    private String mode; // "java" or "fory"
}
