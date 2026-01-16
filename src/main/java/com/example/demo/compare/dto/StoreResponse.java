package com.example.demo.compare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {
    private String sessionId;
    private int sizeKb;
    private int javaBytesSize;
    private int foryBytesSize;
}
