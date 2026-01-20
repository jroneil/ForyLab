package com.example.demo.compare.dto;

import lombok.Data;

@Data
public class BenchRequest {

    // ===== ORIGINAL FIELDS (UNCHANGED) =====
    private int iterations;
    private int warmup; // iterations for JIT warm-up
    private String mode; // "java" or "fory"
    private String type; // "serialize" or "deserialize"
    private boolean circular;
    private String backend; // "memory", "jdbc", "redis"

    // ===== OPTIONAL HELPERS (NON-BREAKING) =====

    /** Default to 100 iterations if client sends 0 or negative */
    public int resolvedIterations() {
        return iterations > 0 ? iterations : 100;
    }

    /** Default to 10 warmup iterations */
    public int resolvedWarmup() {
        return warmup >= 0 ? warmup : 10;
    }

    /** Default benchmark type */
    public String resolvedType() {
        return (type == null || type.isBlank()) ? "deserialize" : type;
    }

    /** Defensive normalization */
    public String resolvedMode() {
        return mode == null ? null : mode.toLowerCase();
    }

    /** Lightweight validation */
    public void validate() {
        String m = resolvedMode();
        String t = resolvedType();

        if (!"java".equals(m) && !"fory".equals(m)) {
            throw new IllegalArgumentException("mode must be 'java' or 'fory'");
        }
        if (!"serialize".equals(t) && !"deserialize".equals(t)) {
            throw new IllegalArgumentException("type must be 'serialize' or 'deserialize'");
        }
    }
}
