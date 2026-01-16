package com.example.demo.compare.domain;

import java.util.UUID;

public class QuoteFactory {

    public static Quote createLargeQuote(int sizeKb) {
        Quote quote = new Quote();
        quote.setId(UUID.randomUUID().toString());
        quote.setQuoteNumber("Q-" + System.currentTimeMillis());
        quote.setCustomerName("John Doe");
        quote.setTotalPremium(1250.50);

        // Approximately 1KB of content per iteration if we add enough strings
        // Each character is 2 bytes in memory, but serializes to 1-3 bytes in UTF-8
        // For simplicity, we'll repeat strings until we reach the approximate size.

        String largePadding = "A".repeat(500); // ~0.5 KB string

        int itemsToAdd = Math.max(1, (sizeKb / 2)); // Adjusting to get closer to sizeKb

        for (int i = 0; i < itemsToAdd; i++) {
            quote.getCoverages().add(
                    new Coverage("Type-" + i, "Comprehensive coverage with padding " + largePadding, 100000, 50.0));
            quote.getDrivers().add(new Driver("Driver-" + i, "LIC-" + i, 30 + (i % 50),
                    "Notes for driver " + i + ": " + largePadding));
            quote.getVehicles().add(new Vehicle("VIN-" + i, "Make-" + i, "Model-" + i, 2020 + (i % 5),
                    "Vehicle description " + i + ": " + largePadding));
        }

        quote.setNotes("General notes: " + largePadding.repeat(Math.max(1, sizeKb / 10)));

        return quote;
    }
}
