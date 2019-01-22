package com.flowly4j.repository;

import com.flowly4j.repository.model.Session;
import com.flowly4j.variables.Variables;
import io.vavr.control.Either;

public interface Repository {

    Either<Throwable, Session> create(Variables initialVariables);

    Either<Throwable, Session> get(String sessionId);

    Either<Throwable, Session> save(Session session);

}
