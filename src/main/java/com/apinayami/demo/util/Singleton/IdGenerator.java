package com.apinayami.demo.util.Singleton;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {
    private static IdGenerator instance;

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        if (instance == null) {
            synchronized (IdGenerator.class) {
                if (instance == null) {
                    instance = new IdGenerator();
                }
            }
        }
        return instance;
    }

    public String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}

