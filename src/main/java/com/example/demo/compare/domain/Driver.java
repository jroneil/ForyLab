package com.example.demo.compare.domain;

import java.io.Serializable;

public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String licenseNumber;
    private int age;
    private String notes;

    public Driver() {
    }

    public Driver(String name, String licenseNumber, int age, String notes) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.age = age;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
