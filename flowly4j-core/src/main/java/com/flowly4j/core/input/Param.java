package com.flowly4j.core.input;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;

/**
 * Represent a pair key -> value
 */
@Getter
@ToString
public class Param {

    private String key;
    private Object value;

    private Param(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static <T> Param of(Key<T> key, T value) {
        return new Param(key.getIdentifier(), value);
    }

}
