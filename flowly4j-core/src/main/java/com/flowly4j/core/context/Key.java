package com.flowly4j.core.context;

public interface Key<T> {

    String identifier();

    static <T> Key<T> of(String identifier) {
        return () -> identifier;
    }

}
