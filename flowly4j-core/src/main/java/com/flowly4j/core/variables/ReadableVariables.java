package com.flowly4j.core.variables;

import io.vavr.Function0;
import io.vavr.control.Option;

import java.util.function.Predicate;

/**
 * Read-only interface of Variables
 */
public interface ReadableVariables {

    <T> Option<T> get(Key<T> key);

    <T> T getOrElse(Key<T> key, Function0<T> orElse);

    Boolean contains(Key<?> key);

    <T> Boolean exists(Key<T> key, Predicate<? super T> condition);

}
