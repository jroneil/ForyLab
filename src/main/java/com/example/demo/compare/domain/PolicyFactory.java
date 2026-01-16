package com.example.demo.compare.domain;

import java.util.UUID;

public class PolicyFactory {

    public static InsurancePolicy createPolicy(int sizeKb, boolean circular) {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyNumber("POL-" + UUID.randomUUID().toString().substring(0, 8));
        policy.setInsuredName("Large Corp Industries");
        policy.setEffectiveDate("2024-01-01");

        // Approximate size by adding coverages and endorsements
        int targetBytes = sizeKb * 1024;
        int currentBytes = 0;

        int i = 0;
        while (currentBytes < targetBytes) {
            String coverageName = "Global General Liability Level " + i;
            String desc = "Comprehensive coverage for various business risks including property damage, bodily injury, and legal defense costs across multiple jurisdictions. "
                    + i;
            Coverage c = new Coverage(coverageName, desc, 1000000.0 * (i + 1), 5000.0);
            policy.getCoverages().add(c);

            String endorsement = "Endorsement #" + i
                    + ": Special terms for high-risk zones and additional insured parties. " + desc;
            policy.getEndorsements().add(endorsement);

            policy.getAttributes().put("attr-" + i, "val-" + UUID.randomUUID().toString());

            // Very rough estimate of overhead
            currentBytes += (coverageName.length() + desc.length() + endorsement.length() + 100);
            i++;
            if (i > 10000)
                break; // safety
        }

        return policy;
    }
}
