package com.example.demo.compare.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
public class StoreResponse {
    private String sessionId;
    private int sizeKb;
    private int javaBytesSize;
    private int foryBytesSize;
    private String objectType;
    private String objectClass;
    private Map<String, Integer> summary;

    public StoreResponse(String sessionId, int sizeKb, int javaBytesSize, int foryBytesSize, String objectType,
            String objectClass, Map<String, Integer> summary) {
        this.sessionId = sessionId;
        this.sizeKb = sizeKb;
        this.javaBytesSize = javaBytesSize;
        this.foryBytesSize = foryBytesSize;
        this.objectType = objectType;
        this.objectClass = objectClass;
        this.summary = summary;
    }
}
