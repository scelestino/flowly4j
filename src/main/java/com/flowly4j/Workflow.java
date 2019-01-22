package com.flowly4j;

import com.flowly4j.errors.ExecutionError;
import com.flowly4j.errors.SessionCantBeExecuted;
import com.flowly4j.errors.TaskNotFound;
import com.flowly4j.repository.Repository;
import com.flowly4j.repository.model.Execution;
import com.flowly4j.repository.model.Session;
import com.flowly4j.repository.model.Status;
import com.flowly4j.tasks.Task;
import com.flowly4j.variables.Variables;
import io.vavr.CheckedConsumer;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.joda.time.DateTime;

import java.util.function.Consumer;

public class Workflow {

    private Task initialTask;

    private Repository repository;

    public Workflow(Task initialTask, Repository repository) {
        this.initialTask = initialTask;
        this.repository = repository;
    }

    public String init(/* params */) {
        // TODO: init session with params and return session id
        return repository.create(new Variables(HashMap.empty())).id;
    }

    public ExecutionResult execute(String sessionId /* params */) {

        // Get Session
        Session session = repository.get(sessionId);

        // Can be executed?
        if(!session.isExecutable()) {
            throw new SessionCantBeExecuted(sessionId);
        }

        // Get current Task
        String taskId = session.lastExecution.map(Execution::taskId).getOrElse(initialTask.id());
        Task currentTask = tasks().find(task -> task.id().equals(taskId)).getOrElseThrow( () -> new TaskNotFound(taskId) );

        // TODO: merge variables (params + session)
        Variables currentVariables = session.variables;

        return execute(currentTask, session, currentVariables);

    }

    private ExecutionResult execute(Task task, Session session, Variables variables) {

        Session currentSession = repository.save(session.running(task, variables));

        task.execute(currentSession.id, variables);

        // TODO: match execute result

        return new ExecutionResult(session.id, task.id(), variables, Status.FINISHED);

    }




    /**
     * It returns a list of every {@link Task} in this workflow
     *
     * @return list of {@link Task}
     */
    private List<Task> tasks() {
        return tasks(initialTask, List.empty());
    }
    private static List<Task> tasks(Task currentTask, List<Task> accum) {
        return accum.contains(currentTask) ? accum : currentTask.followedBy().foldRight( accum.append(currentTask), Workflow::tasks);
    }

}
