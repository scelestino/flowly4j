package com.flowly4j.mariadb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Session;
import io.vavr.collection.Iterator;
import io.vavr.control.Option;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public class MariaDBRepository implements Repository {

    private EntityManagerFactory entityManagerFactory;
    private ObjectMapper objectMapper;

    public MariaDBRepository(EntityManagerFactory entityManagerFactory, ObjectMapper objectMapper) {
        this.entityManagerFactory = entityManagerFactory;
        this.objectMapper = objectMapper;
        //this.objectMapper.addMixIn(Session.class, SessionMixIn.class);
    }

    /**
     * Load a workflow session by sessionId
     */
    @Override
    public Option<Session> get(String sessionId) {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            SessionWrapper session = entityManager.find(SessionWrapper.class, sessionId);
            entityManager.close();
            return Option.of(session.toSession());
        } catch (Throwable throwable) {
            throw new PersistenceException("Error getting session " + sessionId, throwable);
        }
    }

    @Override
    public Session insert(Session session) {
        return null;
    }

    @Override
    public Session update(Session session) {
        return null;
    }

    @Override
    public Iterator<String> getToRetry() {
        return null;
    }
}
