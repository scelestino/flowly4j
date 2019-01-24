package com.flowly4j.example;

import com.flowly4j.core.errors.SessionNotFound;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.repository.model.Session;
import com.flowly4j.core.variables.Variables;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryRepository implements Repository {

    private Map<String, Session> storage = new HashMap<>();

    @Override
    public Session create(Variables initialVariables) {
        String id = UUID.randomUUID().toString();
        return save(new Session(id, initialVariables));
    }

    @Override
    public Session get(String sessionId) {
        if(!storage.containsKey(sessionId)) {
            throw new SessionNotFound(sessionId);
        }
        return storage.get(sessionId);
    }

    @Override
    public Session save(Session session) {
        storage.put(session.id, session);
        return session;
    }

}
