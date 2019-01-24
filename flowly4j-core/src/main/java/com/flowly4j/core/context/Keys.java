package com.flowly4j.core.context;

public class Keys {

    public static <T> Key<T> create(String identifier) {
        return () -> identifier;
    }

}
