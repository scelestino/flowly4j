package com.flowly4j.core.repository;

import com.flowly4j.core.session.Session;
import io.vavr.collection.Iterator;
import io.vavr.control.Option;

/**
 * Interface to implement a repository for workflow sessions
 */
public interface Repository {

    /**
     * Load a workflow session by sessionId
     */
    Option<Session> get(String sessionId);

    /**
     * Insert a new session into the database
     */
    Session insert(Session session);

    /**
     * Update an existent session
     */
    Session update(Session session);

    /**
     * Get sessions to Retry
     */
    Iterator<String> getToRetry();

}
