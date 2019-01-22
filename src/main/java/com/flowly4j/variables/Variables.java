package com.flowly4j.variables;

import io.vavr.Function0;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.ToString;

import java.util.function.Predicate;

@ToString
public class Variables implements ReadableVariables {

    private Map<String, Object> underlying;

    public Variables(Map<String, Object> underlying) {
        this.underlying = underlying;
    }

    public <T> Option<T> get(Key<T> key) {
        return (Option<T>) this.underlying.get(key.identifier());
    }

    public <T> T getOrElse(Key<T> key, Function0<T> orElse) {
        Option<T> opt = get(key);
        return opt.getOrElse(orElse.apply());
    }

    public Boolean contains(Key<?> key) {
        return this.underlying.containsKey(key.identifier());
    }

    public <T> Boolean exists(Key<T> key, Predicate<? super T> condition) {
        Option<T> opt = get(key);
        return opt.exists(condition);
    }

    public <T> Variables set(Key<T> key, T value) {
        return new Variables(this.underlying.put(key.identifier(), value));
    }

    public Variables unset(Key<?> key) {
        return new Variables(this.underlying.remove(key.identifier()));
    }

}
