package com.flowly4j.mariadb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import io.vavr.collection.Iterator;
import io.vavr.control.Option;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.Instant;

public class MariaDBRepository implements Repository {

    private final EntityManagerFactory entityManagerFactory;
    private final ObjectMapper objectMapper;

    public MariaDBRepository(EntityManagerFactory entityManagerFactory, ObjectMapper objectMapper) {
        this.entityManagerFactory = entityManagerFactory;
        this.objectMapper = objectMapper;
    }

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
        try {

            SessionWrapper sessionWrapper = new SessionWrapper(session, objectMapper);
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SessionWrapper updatedSession = entityManager.merge(sessionWrapper);
            entityManager.getTransaction().commit();
            entityManager.close();

            return updatedSession.toSession(objectMapper);

        } catch (OptimisticLockException ex) {
            throw new OptimisticLockException("Session " + session.getSessionId() + " was modified by another transaction");
        } catch (Throwable throwable) {
            throw new PersistenceException("Error saving session " + session.getSessionId(), throwable);
        }
    }

    @Override
    public Iterator<String> getToRetry() {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
            Root<SessionWrapper> root = criteriaQuery.from(SessionWrapper.class);
            criteriaQuery.select(root.get("sessionId"));
            criteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("status"), Status.TO_RETRY),
                            criteriaBuilder.lessThanOrEqualTo(root.get("attempts").get("nextRetry"), Instant.now())
                    )
            ).orderBy(criteriaBuilder.asc(root.get("attempts").get("nextRetry")));

            return Iterator.ofAll(entityManager.createQuery(criteriaQuery).getResultList());

        } catch (Throwable throwable) {
            throw new PersistenceException("Error getting sessions to retry", throwable);
        }
    }
}
