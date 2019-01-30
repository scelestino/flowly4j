package com.flowly4j.core.input;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.ToString;
import lombok.Value;

/**
 * Bind a type to a specific String
 */
@Value(staticConstructor = "of")
public class Key<T> {
    private String identifier;
    private TypeReference<T> typeReference;
}
