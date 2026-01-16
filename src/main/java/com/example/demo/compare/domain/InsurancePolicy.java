package com.example.demo.compare.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsurancePolicy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String policyNumber;
    private String insuredName;
    private String effectiveDate;
    private List<Coverage> coverages = new ArrayList<>();
    private List<String> endorsements = new ArrayList<>();
    private Map<String, String> attributes = new HashMap<>();

    public InsurancePolicy() {
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<Coverage> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<Coverage> coverages) {
        this.coverages = coverages;
    }

    public List<String> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(List<String> endorsements) {
        this.endorsements = endorsements;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
