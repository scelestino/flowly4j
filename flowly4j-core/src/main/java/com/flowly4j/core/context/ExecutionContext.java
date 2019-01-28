package com.flowly4j.core.context;

import com.flowly4j.core.input.Param;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.serialization.Serializer;
import com.flowly4j.core.session.Session;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.ToString;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Execution Context is used as a bridge between Session and Tasks
 */
@ToString(exclude = "serializer")
public class ExecutionContext implements ReadableExecutionContext, WritableExecutionContext {

    private String sessionId;
    private Map<String, Object> variables;
    private Serializer<String> serializer;

    private ExecutionContext(String sessionId, Map<String, Object> variables, Serializer<String> serializer) {
        this.sessionId = sessionId;
        this.variables = variables;
        this.serializer = serializer;
    }

    public <T> Option<T> get(Key<T> key) {
        //return variables.get(key.identifier()).map(serializer::deepCopy);
        return variables.get(key.identifier()).map( d -> serializer.read(serializer.write(d)) );
    }

    public <T> T getOrElse(Key<T> key, Supplier<T> orElse) {
        return get(key).getOrElse(orElse);
    }

    public <T> void set(Key<T> key, T value) {
        variables = variables.put(key.identifier(), serializer.deepCopy(value));
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

    public <T> Boolean forAll(Key<T> key, Predicate<? super T> condition) {
        return get(key).forAll(condition);
    }

    public static ExecutionContext of(Serializer<String> serializer, Session session, Param... params) {
        Map<String, Object> variables = List.of(params).toMap(p -> Tuple.of(p.getKey(), p.getValue())).merge(session.getVariables());
        return new ExecutionContext(session.getSessionId(), variables, serializer);
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public Map<String, Object> getVariables() {
        //return serializer.deepCopy(variables);
        return variables;
    }

}
