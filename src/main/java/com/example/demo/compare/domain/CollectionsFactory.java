package com.example.demo.compare.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CollectionsFactory {

    public static CollectionsBlob createCollectionsBlob(int sizeKb, boolean circular) {
        CollectionsBlob blob = new CollectionsBlob();

        int targetBytes = sizeKb * 1024;
        int currentBytes = 0;

        if (circular) {
            blob.getMap().put("_self", blob);
        }

        int i = 0;
        while (currentBytes < targetBytes) {
            String key = "key-" + i + "-" + UUID.randomUUID().toString();
            String val = "Extremely long string value to fill up the memory and provide a realistic serialization payload for performance testing purposes in this Serialization Lab environment. "
                    + i;

            blob.getMap().put(key, val);
            blob.getStrings().add(val);

            Map<String, Object> item = new HashMap<>();
            item.put("id", i);
            item.put("data", val);
            blob.getItems().add(item);

            currentBytes += (key.length() + val.length() * 3 + 150);
            i++;
            if (i > 10000)
                break; // safety
        }

        return blob;
    }
}
