package com.flowly4j.mariadb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "sessions")
@NoArgsConstructor
@Getter
public class SessionWrapper {
    @Id
    @Column(name = "session_id")
    private String sessionId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_variables", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "variable_value")
    private Map<String, String> variables;

    private ExecutionWrapper lastExecution;

    private AttemptsWrapper attempts;

    @Column(name = "create_at")
    private Instant createAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Version
    private Long version;

    public SessionWrapper(Session session, ObjectMapper objectMapper) {
        this.sessionId = session.getSessionId();
        this.variables = session.getVariables().mapValues(v -> {
            try {
                return objectMapper.writeValueAsString(v);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toJavaMap();
        this.lastExecution = session.getLastExecution().map(ExecutionWrapper::new).getOrNull();
        this.attempts = session.getAttempts().map(AttemptsWrapper::new).getOrNull();
        this.createAt = session.getCreateAt();
        this.status = session.getStatus();
        this.version = session.getVersion();
    }

    public Session toSession(ObjectMapper objectMapper) {
        return new Session(
                sessionId,
                io.vavr.collection.HashMap.ofAll(this.variables).mapValues(v -> {
                    try {
                        return objectMapper.readValue(v, Object.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                Option.of(lastExecution).map(ExecutionWrapper::toExecution),
                Option.of(attempts).map(AttemptsWrapper::toAttempts),
                createAt,
                status,
                version
        );
    }
}
