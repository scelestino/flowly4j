package com.flowly4j.mariadb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "sessions")
@NoArgsConstructor
public class SessionWrapper {
    @Id
    @Column(name = "session_id")
    private String sessionId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_variables", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "variable_value")
    private Map<String, String> variables; //TODO SOLN: que se va a guardar en variable_value, un json?
    //La solucion que planteo Mariano Alvarez fue guardar un Map<String,String> y dsp serializar ese objeto
    // Para no modificar la implementacion de flowly, deberiamos siempre devolver objetos en las variables

    private ExecutionWrapper lastExecution; // TODO SOLN: OPTION

    private AttemptsWrapper attempts; // TODO SOLN: OPTION

    @Column(name = "create_at")
    private Instant createAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Version
    private Long version;

    public SessionWrapper(Session session, ObjectMapper objectMapper) {
        this.sessionId = session.getSessionId();
        //TODO SOLN: toString no deberia ser deberia usarse el object mapper o algo
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
                //TODO SOLN: aca deberiamos hacer alguna transformacion?
                io.vavr.collection.HashMap.ofAll(this.variables).mapValues(v -> {
                    try {
                        return objectMapper.readValue(v, Object.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                Option.of(lastExecution.toExecution()),
                Option.of(attempts.toAttempts()),
                createAt,
                status,
                version
        );
    }
}
