package com.flowly4j.core;

import com.flowly4j.core.errors.ExecutionError;
import com.flowly4j.core.errors.SessionCantBeExecuted;
import com.flowly4j.core.errors.TaskNotFound;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.repository.model.Execution;
import com.flowly4j.core.repository.model.Session;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.results.TaskResult;
import com.flowly4j.core.variables.Variables;
import io.vavr.collection.List;

import static com.flowly4j.core.tasks.results.TaskResultPatterns.*;
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

    public String init(Param ...params) {
        return repository.create(new Variables(List.of(params).toMap(Param::toTuple))).id;
    }

    public ExecutionResult execute(String sessionId, Param ...params) {

        // Get Session
        Session session = repository.get(sessionId);

        // Can be executed?
        if (!session.isExecutable()) {
            throw new SessionCantBeExecuted(sessionId);
        }

        // Get current Task
        String taskId = session.lastExecution.map(Execution::taskId).getOrElse(initialTask.id());
        Task currentTask = tasks().find(task -> task.id().equals(taskId)).getOrElseThrow(() -> new TaskNotFound(taskId));

        // Merge old with new variables
        Variables currentVariables = session.variables.merge(new Variables(List.of(params).toMap(Param::toTuple)));

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
