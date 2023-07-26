package com.flowly4j.mariadb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Session;
import io.vavr.collection.Iterator;
import io.vavr.control.Option;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

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
            return Option.of(session.toSession(objectMapper));
        } catch (Throwable throwable) {
            throw new PersistenceException("Error getting session " + sessionId, throwable);
        }
    }

    @Override
    public Session insert(Session session) {
        try {
            SessionWrapper sessionWrapper = new SessionWrapper(session, objectMapper);
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(sessionWrapper);
            entityManager.getTransaction().commit();
            entityManager.close();
            return session;
        } catch (Throwable throwable) {
            throw new PersistenceException("Error inserting session " + session.getSessionId(), throwable);
        }
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
