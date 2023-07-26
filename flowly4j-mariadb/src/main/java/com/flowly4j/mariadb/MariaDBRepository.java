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
        //TODO SOLN: revisar para que sirve el mixin y si es necesario en hibernate
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

    /*
    // Update method using SessionWrapper
    public Session update(Session session) {
        try {
            SessionWrapper sessionWrapper = new SessionWrapper(session);
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SessionWrapper existingSession = entityManager.find(SessionWrapper.class, session.getSessionId());
            if (existingSession == null || !existingSession.getVersion().equals(session.getVersion())) {
                entityManager.getTransaction().rollback();
                throw new OptimisticLockException("Session " + session.getSessionId() + " was modified by another transaction");
            }

            entityManager.merge(sessionWrapper);
            entityManager.getTransaction().commit();
            entityManager.close();
            return session;
        } catch (OptimisticLockException ex) {
            throw ex;
        } catch (Throwable throwable) {
            throw new PersistenceException("Error saving session " + session.getSessionId(), throwable);
        }
    }

    // Retrieve method using SessionWrapper
    public List<String> getToRetry() {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
            Root<SessionWrapper> root = criteriaQuery.from(SessionWrapper.class);

            // Build the criteria to retrieve sessions that need to be retried
            criteriaQuery.select(root.get("sessionId")).where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("status"), Status.TO_RETRY),
                            criteriaBuilder.lessThanOrEqualTo(root.get("attempts").get("nextRetry"), Instant.now())
                    )
            );

            TypedQuery<String> query = entityManager.createQuery(criteriaQuery);
            List<String> sessionIds = query.getResultList();
            entityManager.close();
            return sessionIds;
        } catch (Throwable throwable) {
            throw new PersistenceException("Error getting sessions to retry", throwable);
        }
    }*/
}
