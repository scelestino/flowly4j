package com.flowly4j.core.repository;

import com.flowly4j.core.session.Session;

public interface Repository {

    Session get(String sessionId);

    Session insert(Session session);

    Session update(Session session);

}
