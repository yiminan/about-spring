package com.example.aboutspring.properties;

import org.springframework.stereotype.Component;

@Component
public class PropertiesExample {
    private final Properties properties;

    public PropertiesExample(Properties properties) {
        this.properties = properties;
    }
}
