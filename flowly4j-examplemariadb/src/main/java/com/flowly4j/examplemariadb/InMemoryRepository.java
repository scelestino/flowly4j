package com.flowly4j.examplemariadb;

import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Attempts;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import io.vavr.collection.Iterator;
import io.vavr.control.Option;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository implements Repository {

    private Map<String, Session> storage = new HashMap<>();

    @Override
    public Option<Session> get(String sessionId) {
        return Option.of(storage.get(sessionId));
    }

    @Override
    public Session insert(Session session) {
        return storage.put(session.getSessionId(), session);
    }

    @Override
    public Session update(Session session) {
        return storage.put(session.getSessionId(), session);
    }

    @Override
    public Iterator<String> getToRetry() {
        return io.vavr.collection.HashMap.ofAll(storage).values().filter( s -> s.getStatus().equals(Status.TO_RETRY) && s.getAttempts().flatMap(Attempts::getNextRetry).exists( d -> d.isBefore(Instant.now()))).map(Session::getSessionId).iterator();
    }

}
