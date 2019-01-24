package com.flowly4j.core;

import com.flowly4j.core.variables.Key;
import io.vavr.Tuple;
import io.vavr.Tuple2;

public class Param {

    public final String key;
    public final Object value;

    private Param(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static <T> Param of(Key<T> key, T value) {
        return new Param(key.identifier(), value);
    }

    public static Tuple2<String, Object> toTuple(Param param) {
        return Tuple.of(param.key, param.value);
    }

}
