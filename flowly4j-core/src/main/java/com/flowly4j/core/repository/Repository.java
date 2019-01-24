package com.flowly4j.core.repository;

import com.flowly4j.core.repository.model.Session;
import com.flowly4j.core.variables.Variables;

public interface Repository {

    Session create(Variables initialVariables);

    Session get(String sessionId);

    Session save(Session session);

}
