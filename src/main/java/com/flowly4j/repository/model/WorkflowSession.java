package com.flowly4j.repository.model;

import com.flowly4j.variables.Variables;
import io.vavr.control.Option;
import org.joda.time.DateTime;


public class WorkflowSession {

    public String id;
    public Variables variables;
    public Option<Execution> lastExecution;
    public Option<Cancellation> cancellation;
    public DateTime createAt;
    public String status;

    public WorkflowSession(String id, Variables variables, Option<Execution> lastExecution, Option<Cancellation> cancellation, DateTime createAt, String status) {
        this.id = id;
        this.variables = variables;
        this.lastExecution = lastExecution;
        this.cancellation = cancellation;
        this.createAt = createAt;
        this.status = status;
    }

    public WorkflowSession(String id, Variables variables) {
        this.id = id;
        this.variables = variables;
        this.lastExecution = Option.none();
        this.cancellation = Option.none();
        this.createAt = DateTime.now();
        this.status = Status.CREATED;
    }

    public Boolean isExecutable() {
        switch (this.status) {
            case Status.RUNNING:
            case Status.FINISHED:
            case Status.CANCELLED:
                return false;
            default: return true;
        }
    }

}
