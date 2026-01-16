package com.example.demo.compare.domain;

import java.io.Serializable;

public class Coverage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String type;
    private String description;
    private double limit;
    private double premium;

    public Coverage() {}

    public Coverage(String type, String description, double limit, double premium) {
        this.type = type;
        this.description = description;
        this.limit = limit;
        this.premium = premium;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }
    public double getPremium() { return premium; }
    public void setPremium(double premium) { this.premium = premium; }
}
