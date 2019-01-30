package com.flowly4j.core.errors;

import lombok.Getter;

@Getter
public class KeyNotFoundException extends RuntimeException {

    private String key;

    public KeyNotFoundException(String key, String message) {
        super(message);
        this.key = key;
    }

}
