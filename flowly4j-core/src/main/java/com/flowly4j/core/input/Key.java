package com.flowly4j.core.input;

/**
 * Bind a type to a specific String
 */
public interface Key<T> {

    String identifier();

    static <T> Key<T> of(String identifier) {
        return () -> identifier;
    }

}
