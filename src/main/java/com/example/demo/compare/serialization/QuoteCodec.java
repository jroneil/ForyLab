package com.example.demo.compare.serialization;

import com.example.demo.compare.domain.Quote;
import org.apache.fory.ThreadSafeFory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuoteCodec {

    private final ThreadSafeFory fory;

    @Autowired
    public QuoteCodec(ThreadSafeFory fory) {
        this.fory = fory;
    }

    public byte[] serialize(Quote quote) {
        return fory.serialize(quote);
    }

    public Quote deserialize(byte[] data) {
        return (Quote) fory.deserialize(data);
    }
}
