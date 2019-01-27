package com.flowly4j.core.context;

import com.flowly4j.core.input.Key;
import io.vavr.control.Option;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface WritableExecutionContext extends ReadableExecutionContext {

    <T> void set(Key<T> key, T value);

    void unset(Key<?> key);

}
