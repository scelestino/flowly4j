package com.flowly4j.core.context;

import com.flowly4j.core.Json;
import com.flowly4j.core.Param;
import com.flowly4j.core.repository.model.Session;
import io.vavr.control.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ExecutionContext implements ExecutionVariables {

    public final String sessionId;
    private final Map<String, Object> variables;

    private ExecutionContext(String sessionId, Map<String, Object> variables) {
        this.sessionId = sessionId;
        this.variables = variables;
    }

    public <T> Option<T> get(Key<T> key) {
        if(variables.containsKey(key.identifier())) {
            return Option.of(Json.as(variables.get(key.identifier())));
        } else {
            return Option.none();
        }
    }

    public <T> T getOrElse(Key<T> key, Supplier<T> orElse) {
        return get(key).getOrElse(orElse);
    }

    public <T> void set(Key<T> key, T value) {
        variables.put(key.identifier(), value);
    }

    public void unset(Key<?> key) {
        variables.remove(key.identifier());
    }

    public Boolean contains(Key<?> key) {
        return variables.containsKey(key.identifier());
    }

    public <T> Boolean exists(Key<T> key, Predicate<? super T> condition) {
        return get(key).exists(condition);
    }

    public static ExecutionContext of(Session session, Param...params) {
        // TODO: merge params with session variables
        return new ExecutionContext(session.id, new HashMap<>());
    }

}
