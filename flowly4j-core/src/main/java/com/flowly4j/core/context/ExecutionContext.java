package com.flowly4j.core.context;

import com.flowly4j.core.errors.KeyNotFoundException;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.serialization.Serializer;
import com.flowly4j.core.session.Attempts;
import com.flowly4j.core.session.Session;
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
    private Option<Attempts> attempts;
    private Serializer serializer;

    private ExecutionContext(String sessionId, Map<String, Object> variables, Option<Attempts> attempts, Serializer serializer) {
        this.sessionId = sessionId;
        this.variables = variables;
        this.attempts = attempts;
        this.serializer = serializer;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public <T> Option<T> get(Key<T> key) {
        return variables.get(key.getIdentifier()).map(d -> serializer.deepCopy(d, key.getTypeReference()));
    }

    public <T> T getOrElse(Key<T> key, Supplier<T> orElse) {
        return get(key).getOrElse(orElse);
    }

    public <T> T getOrElseThrow(Key<T> key) {
        return get(key).getOrElseThrow(() -> new KeyNotFoundException(key.getIdentifier(), "Key not found in Execution Context"));
    }

    public <T, X extends Throwable> T getOrElseThrow(Key<T> key, Supplier<X> throwable) throws X {
        return get(key).getOrElseThrow(throwable);
    }

    public <T> ExecutionContext set(Key<T> key, T value) {
        variables = variables.put(key.getIdentifier(), serializer.deepCopy(value, key.getTypeReference()));
        return this;
    }

    public ExecutionContext unset(Key<?> key) {
        variables = variables.remove(key.getIdentifier());
        return this;
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

    public Map<String, Object> getVariables() {
        return variables;
    }

    public Option<Attempts> getAttempts() {
        return attempts;
    }

    public ExecutionContext copy() {
        return new ExecutionContext(sessionId, variables, attempts, serializer);
    }

    public static class ExecutionContextFactory {

        private Serializer serializer;

        public ExecutionContextFactory(Serializer serializer) {
            this.serializer = serializer;
        }

        public ExecutionContext create(Session session) {
            return new ExecutionContext(session.getSessionId(), session.getVariables(), session.getAttempts(), serializer);
        }

    }

}
