package com.flowly4j.core.errors;

import lombok.Getter;

@Getter
public class KeyNotFoundException extends RuntimeException {

    private String key;

    public KeyNotFoundException(String key) {
        super(String.format("Key %s not found in Execution Context", key));
        this.key = key;
    }

}
