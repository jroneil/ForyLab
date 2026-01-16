package com.example.demo.compare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadResponse {
    private Object data;
    private long deserializationTimeNano;
    private double deserializationTimeMs;
}
