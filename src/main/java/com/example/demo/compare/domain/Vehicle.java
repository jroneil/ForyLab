package com.example.demo.compare.domain;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vin;
    private String make;
    private String model;
    private int year;
    private String description;

    public Vehicle() {
    }

    public Vehicle(String vin, String make, String model, int year, String description) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.description = description;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
