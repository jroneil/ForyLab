package com.example.demo.compare.dto;

import com.example.demo.compare.domain.Quote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadResponse {
    private Quote quote;
    private long deserializationTimeNano;
    private double deserializationTimeMs;
}
