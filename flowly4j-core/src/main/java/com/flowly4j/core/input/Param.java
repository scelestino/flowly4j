package com.flowly4j.core.input;

import lombok.*;


/**
 * Represent a pair key -> value
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Param {

    Key key;
    Object value;

    public static <T> Param of(Key<T> key, T obj) {
        return new Param(key, obj);
    }

}
