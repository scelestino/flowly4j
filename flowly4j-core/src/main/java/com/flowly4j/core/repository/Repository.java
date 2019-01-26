package com.flowly4j.core.repository;

import com.flowly4j.core.session.Session;

/**
 * Interface to implement a repository for workflow sessions
 */
public interface Repository {

    /**
     * Load a workflow session by sessionId
     */
    Session get(String sessionId);

    /**
     * Insert a new session into the database
     */
    Session insert(Session session);

    /**
     * Update an existent session
     */
    Session update(Session session);

}
