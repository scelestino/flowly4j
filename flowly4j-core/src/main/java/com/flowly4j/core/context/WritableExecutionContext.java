package com.flowly4j.core.context;

import com.flowly4j.core.input.Key;

public interface WritableExecutionContext extends ReadableExecutionContext {

    <T> ExecutionContext set(Key<T> key, T value);

    ExecutionContext unset(Key<?> key);

}
