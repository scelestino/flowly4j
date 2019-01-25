package com.flowly4j.core;

import com.flowly4j.core.errors.ExecutionError;
import com.flowly4j.core.errors.SessionCantBeExecuted;
import com.flowly4j.core.errors.TaskNotFound;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;

import java.util.UUID;

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

    /**
     * Initialize a new workflow session
     */
    public String init(Param... params) {
        return repository.save(Session.of(UUID.randomUUID().toString(), params)).id;
//        return repository.save(Session.of(null, params)).id;
    }

    /**
     * Execute an instance of {@link Workflow} form its current {@link Task} with the given params
     *
     */
    public ExecutionResult execute(String sessionId, Param ...params) {

        // Get Session
        Session session = repository.get(sessionId);

        // Can be executed?
        if (!session.isExecutable()) {
            throw new SessionCantBeExecuted(sessionId);
        }

        // Get current Task
        String taskId = session.lastExecution.map( e -> e.taskId ).getOrElse(initialTask.id());
        Task currentTask = tasks().find(task -> task.id().equals(taskId)).getOrElseThrow(() -> new TaskNotFound(taskId));

        // Create Execution Context
        ExecutionContext executionContext = ExecutionContext.of(session, params);

        // Execute
        return execute(currentTask, session, executionContext);

    }

    private ExecutionResult execute(Task task, Session session, ExecutionContext executionContext) {

        System.out.println("EXECUTING " + task.id());

        // Set the session as running
        Session currentSession = repository.save(session.running(task, executionContext));

        // Execute the current task
        TaskResult taskResult = task.execute(executionContext);

        return Match(taskResult).of(

                Case($Continue($()), nextTask -> execute(nextTask, currentSession, executionContext)),

                Case($Block, () -> ExecutionResult.of(repository.save(currentSession.blocked(task)), task)),

                Case($Finish, () -> ExecutionResult.of(repository.save(currentSession.finished(task)), task)),

                Case($OnError($()), cause -> {
                    throw new ExecutionError(cause, repository.save(currentSession.onError(task, cause)), task);
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
