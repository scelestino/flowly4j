package com.flowly4j.core.errors;

public class KeyNotFoundException extends RuntimeException {

    private String key;

    public KeyNotFoundException(String key, String message) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
