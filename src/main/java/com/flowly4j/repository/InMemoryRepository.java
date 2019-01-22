package com.flowly4j.repository;

import com.flowly4j.errors.SessionNotFound;
import com.flowly4j.repository.model.Session;
import com.flowly4j.variables.Variables;
import io.vavr.control.Either;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryRepository implements Repository {

    private Map<String, Session> storage = new HashMap<>();

    @Override
    public Either<Throwable, Session> create(Variables initialVariables) {
        String id = UUID.randomUUID().toString();
        return save(new Session(id, initialVariables));
    }

    @Override
    public Either<Throwable, Session> get(String sessionId) {
        return storage.containsKey(sessionId) ? Either.right(storage.get(sessionId)) : Either.left(new SessionNotFound(sessionId));
    }

    @Override
    public Either<Throwable, Session> save(Session session) {
        return Either.right(storage.put(session.id, session));
    }

}
