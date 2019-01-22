package com.flowly4j.repository;

import com.flowly4j.repository.model.Session;
import com.flowly4j.variables.Variables;

public interface Repository {

    Session create(Variables initialVariables);

    Session get(String sessionId);

    Session save(Session session);

}
