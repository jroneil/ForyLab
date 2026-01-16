package com.example.demo.compare.domain;

import java.util.UUID;

public class QuoteFactory {

    public static Quote createLargeQuote(int sizeKb, boolean circular) {
        Quote quote = new Quote();
        quote.setId(UUID.randomUUID().toString());
        quote.setQuoteNumber("Q-" + System.currentTimeMillis());
        quote.setCustomerName("John Doe");
        quote.setTotalPremium(1250.50);

        String largePadding = "A".repeat(500);
        int itemsToAdd = Math.max(1, (sizeKb / 2));

        for (int i = 0; i < itemsToAdd; i++) {
            Driver driver = new Driver("Driver-" + i, "LIC-" + i, 30 + (i % 50),
                    "Notes for driver " + i + ": " + largePadding);

            quote.getDrivers().add(driver);
            quote.getCoverages().add(
                    new Coverage("Type-" + i, "Comprehensive coverage with padding " + largePadding, 100000, 50.0));
            quote.getVehicles().add(new Vehicle("VIN-" + i, "Make-" + i, "Model-" + i, 2020 + (i % 5),
                    "Vehicle description " + i + ": " + largePadding));

            if (circular && i == 0) {
                quote.setPrimaryDriver(driver); // Quote -> Driver
                // Note: Driver doesn't have a back-reference to Quote in its fields,
                // but we could add one if we wanted to be more explicit.
                // However, Quote -> Driver (List) and Quote -> primaryDriver (Field) is already
                // multiple references.
            }
        }

        quote.setNotes("General notes: " + largePadding.repeat(Math.max(1, sizeKb / 10)));

        return quote;
    }
}
