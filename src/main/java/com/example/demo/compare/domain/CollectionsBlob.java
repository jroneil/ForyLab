package com.example.demo.compare.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionsBlob implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Object> map = new HashMap<>();
    private List<Map<String, Object>> items = new ArrayList<>();
    private List<String> strings = new ArrayList<>();

    public CollectionsBlob() {
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }
}
