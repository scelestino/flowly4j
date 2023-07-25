package com.flowly4j.mariadb;

import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "sessions")
@NoArgsConstructor
public class SessionWrapper {
    @Id
    @Column(name = "session_id")
    private String sessionId;

    @ElementCollection
    @CollectionTable(name = "session_variables", joinColumns = @JoinColumn(name = "session_id"))
    @MapKeyColumn(name = "variable_key")
    @Column(name = "variable_value")
    private Map<String, Object> variables; //TODO SOLN: que se va a guardar en variable_value, un json?
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

    public SessionWrapper(Session session) {
        this.sessionId = session.getSessionId();
        this.variables = session.getVariables().toJavaMap();
        this.lastExecution = session.getLastExecution().map(ExecutionWrapper::new).getOrNull();
        this.attempts = session.getAttempts().map(AttemptsWrapper::new).getOrNull();
        this.createAt = session.getCreateAt();
        this.status = session.getStatus();
        this.version = session.getVersion();
    }

    public Session toSession() {
        return new Session(
                sessionId,
                io.vavr.collection.HashMap.ofAll(variables),
                Option.of(lastExecution.toExecution()),
                Option.of(attempts.toAttempts()),
                createAt,
                status,
                version
        );
    }
}
