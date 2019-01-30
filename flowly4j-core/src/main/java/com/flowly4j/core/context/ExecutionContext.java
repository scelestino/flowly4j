package com.flowly4j.core.context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flowly4j.core.errors.KeyNotFoundException;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.serialization.Serializer;
import com.flowly4j.core.session.Session;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.ToString;
import lombok.val;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Execution Context is used as a bridge between Session and Tasks
 */
@ToString(exclude = "serializer")
public class ExecutionContext implements ReadableExecutionContext, WritableExecutionContext {

    private String sessionId;
    private Map<String, Object> variables;
    private Serializer serializer;

    private ExecutionContext(String sessionId, Map<String, Object> variables, Serializer serializer) {
        this.sessionId = sessionId;
        this.variables = variables;
        this.serializer = serializer;
    }

    public <T> Option<T> get(Key<T> key) {
        return variables.get(key.getIdentifier()).map(d -> serializer.deepCopy(d, key.getTypeReference()));
    }

    public <T> T getOrElse(Key<T> key, Supplier<T> orElse) {
        return get(key).getOrElse(orElse);
    }

    public <T> T getOrThrow(Key<T> key) {
        return get(key).getOrElse(() -> {
            throw new KeyNotFoundException(key.getIdentifier(), "Key not found in Execution Context");
        });
    }

    public <T> void set(Key<T> key, T value) {
        variables = variables.put(key.getIdentifier(), serializer.deepCopy(value, key.getTypeReference()));
    }

    public void unset(Key<?> key) {
        variables = variables.remove(key.getIdentifier());
    }

    public Boolean contains(Key<?> key) {
        return variables.containsKey(key.getIdentifier());
    }

    public <T> Boolean exists(Key<T> key, Predicate<? super T> condition) {
        return get(key).exists(condition);
    }

    public <T> Boolean forAll(Key<T> key, Predicate<? super T> condition) {
        return get(key).forAll(condition);
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public Map<String, Object> getVariables() {
        return serializer.deepCopy(variables, new TypeReference<Map<String, Object>>() {});
    }

    public static class ExecutionContextFactory {

        private Serializer serializer;

        public ExecutionContextFactory(Serializer serializer) {
            this.serializer = serializer;
        }

        public ExecutionContext create(Session session, List<Param> params) {
            val variables = params.toMap(p -> Tuple.of(p.getKey().getIdentifier(), p.getValue())).merge(session.getVariables());
            return new ExecutionContext(session.getSessionId(), variables, serializer);
        }

    }

}
