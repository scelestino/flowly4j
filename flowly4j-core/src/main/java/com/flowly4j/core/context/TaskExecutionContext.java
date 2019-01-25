package com.flowly4j.core.context;

import io.vavr.control.Option;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface TaskExecutionContext {

    <T> Option<T> get(Key<T> key);

    <T> T getOrElse(Key<T> key, Supplier<T> orElse);

    Boolean contains(Key<?> key);

    <T> Boolean exists(Key<T> key, Predicate<? super T> condition);

    <T> void set(Key<T> key, T value);

    void unset(Key<?> key);

}
