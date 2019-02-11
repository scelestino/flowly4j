package com.flowly4j.core.input;

import lombok.*;

/**
 * Represent a pair key -> value
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Param {

    private Key key;
    private Object value;

    public static <T> Param of(Key<T> key, T obj) {
        return new Param(key, obj);
    }

}
