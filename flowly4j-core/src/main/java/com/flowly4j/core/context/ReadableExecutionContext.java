package com.flowly4j.core.context;

import com.flowly4j.core.input.Key;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ReadableExecutionContext {

    String getSessionId();

    <T> Option<T> get(Key<T> key);

    <T> T getOrElse(Key<T> key, Supplier<T> orElse);

    <T> T getOrElseThrow(Key<T> key);

    <T, X extends Throwable> T getOrElseThrow(Key<T> key, Supplier<X> throwable) throws X;

    Boolean contains(Key<?> key);

    <T> Boolean exists(Key<T> key, Predicate<? super T> condition);

    <T> Boolean forAll(Key<T> key, Predicate<? super T> condition);

}
