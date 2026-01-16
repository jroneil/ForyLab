package com.example.demo.compare.serialization;

import org.apache.fory.Fory;
import org.apache.fory.ThreadSafeFory;
import org.apache.fory.config.Language;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ForyConfig {

    @Bean
    public ThreadSafeFory fory() {
        return Fory.builder()
                .withLanguage(Language.JAVA)
                .withRefTracking(true)
                .requireClassRegistration(false)
                .withClassLoader(Thread.currentThread().getContextClassLoader())
                .buildThreadSafeFory();
    }
}
