package com.flowly4j.core.input;

import lombok.*;

/**
 * Represent a pair key -> value
 */
@Value(staticConstructor = "of")
public class Param {
    Key key;
    Object value;
}
