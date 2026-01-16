package com.example.demo.compare.serialization;

import org.apache.fory.ThreadSafeFory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericForyCodec {

    private final ThreadSafeFory fory;

    @Autowired
    public GenericForyCodec(ThreadSafeFory fory) {
        this.fory = fory;
    }

    public byte[] serialize(Object obj) {
        return fory.serialize(obj);
    }

    public Object deserialize(byte[] data) {
        return fory.deserialize(data);
    }
}
