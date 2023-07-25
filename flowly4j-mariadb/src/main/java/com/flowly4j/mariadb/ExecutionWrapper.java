package com.flowly4j.mariadb;

import com.flowly4j.core.session.Execution;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Embeddable
public class ExecutionWrapper {

    @Column(name = "execution_task_id")
    private String taskId;

    @Column(name = "execution_time")
    private Instant at;

    @Column(name = "execution_message")
    private Option<String> message; // TODO SOLN: Funciona bien el Option? No deberia ser String?

    public ExecutionWrapper(Execution execution) {
        this.taskId = execution.getTaskId();
        this.at = execution.getAt();
        this.message = execution.getMessage();
    }

    public Execution toExecution() {
        return new Execution(taskId, at, message);
    }

}