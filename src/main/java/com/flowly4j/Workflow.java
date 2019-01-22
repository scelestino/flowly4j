package com.flowly4j;

import com.flowly4j.errors.ExecutionError;
import com.flowly4j.errors.SessionCantBeExecuted;
import com.flowly4j.errors.TaskNotFound;
import com.flowly4j.repository.Repository;
import com.flowly4j.repository.model.Execution;
import com.flowly4j.repository.model.Session;
import com.flowly4j.tasks.Task;
import com.flowly4j.tasks.results.TaskResult;
import com.flowly4j.variables.Variables;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

import static com.flowly4j.tasks.results.TaskResultPatterns.*;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;


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
        if (!session.isExecutable()) {
            throw new SessionCantBeExecuted(sessionId);
        }

        // Get current Task
        String taskId = session.lastExecution.map(Execution::taskId).getOrElse(initialTask.id());
        Task currentTask = tasks().find(task -> task.id().equals(taskId)).getOrElseThrow(() -> new TaskNotFound(taskId));

        // TODO: merge variables (params + session)
        Variables currentVariables = session.variables;

        return execute(currentTask, session, currentVariables);

    }

    private ExecutionResult execute(Task task, Session session, Variables variables) {

        System.out.println("EXECUTING " + task.id());

        Session currentSession = repository.save(session.running(task, variables));

        TaskResult taskResult = task.execute(currentSession.id, variables);

        return Match(taskResult).of(

                Case($Continue($(), $()), (nextTask, currentVariables) -> execute(nextTask, currentSession, currentVariables)),

                Case($Block, () -> ExecutionResult.of(repository.save(session.blocked(task)), task)),

                Case($Finish, () -> ExecutionResult.of(repository.save(session.finished(task)), task)),

                Case($OnError($()), cause -> {
                    throw new ExecutionError(cause, repository.save(session.onError(task, cause)), task);
                })

        );

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
        return accum.contains(currentTask) ? accum : currentTask.followedBy().foldRight(accum.append(currentTask), Workflow::tasks);
    }

}
