package com.example.demo.compare.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quote implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String quoteNumber;
    private String customerName;
    private double totalPremium;
    private List<Coverage> coverages = new ArrayList<>();
    private List<Driver> drivers = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private String notes;
    private Driver primaryDriver; // For circular reference test

    public Quote() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(double totalPremium) {
        this.totalPremium = totalPremium;
    }

    public List<Coverage> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<Coverage> coverages) {
        this.coverages = coverages;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Driver getPrimaryDriver() {
        return primaryDriver;
    }

    public void setPrimaryDriver(Driver primaryDriver) {
        this.primaryDriver = primaryDriver;
    }
}
