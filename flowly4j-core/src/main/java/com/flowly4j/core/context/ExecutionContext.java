package com.flowly4j.core.context;

import com.flowly4j.core.Json;
import com.flowly4j.core.Param;
import com.flowly4j.core.session.Session;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ExecutionContext implements TaskExecutionContext {

    public final String sessionId;
    private Map<String, Object> variables;

    private ExecutionContext(String sessionId, Map<String, Object> variables) {
        this.sessionId = sessionId;
        this.variables = variables;
    }

    public <T> Option<T> get(Key<T> key) {
        return variables.get(key.identifier()).map(Json::as);
    }

    public <T> T getOrElse(Key<T> key, Supplier<T> orElse) {
        return get(key).getOrElse(orElse);
    }

    public <T> void set(Key<T> key, T value) {
        variables = variables.put(key.identifier(), value);
    }

    public void unset(Key<?> key) {
        variables = variables.remove(key.identifier());
    }

    public Boolean contains(Key<?> key) {
        return variables.containsKey(key.identifier());
    }

    public <T> Boolean exists(Key<T> key, Predicate<? super T> condition) {
        return get(key).exists(condition);
    }

    public Map<String, Object> variables() {
        return variables;
    }

    public static ExecutionContext of(Session session, Param... params) {
        Map<String, Object> variables = List.of(params).toMap(p -> Tuple.of(p.key, p.value)).merge(session.variables);
        return new ExecutionContext(session.sessionId, variables);
    }

}
