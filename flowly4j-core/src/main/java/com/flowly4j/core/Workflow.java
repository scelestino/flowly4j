package com.flowly4j.core;

import com.flowly4j.core.errors.ExecutionError;
import com.flowly4j.core.errors.SessionCantBeExecuted;
import com.flowly4j.core.errors.TaskNotFound;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ExecutionContext.ExecutionContextFactory;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.output.ExecutionResult;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Execution;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.tasks.Task;
import io.vavr.collection.List;
import lombok.val;

import static com.flowly4j.core.tasks.results.TaskResultPatterns.*;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;


public class Workflow {

    protected Task initialTask;
    protected Repository repository;
    protected ExecutionContextFactory executionContextFactory;

    public Workflow() {}

    public Workflow(Task initialTask, Repository repository, ExecutionContextFactory executionContextFactory) {
        this.initialTask = initialTask;
        this.repository = repository;
        this.executionContextFactory = executionContextFactory;
    }

    /**
     * Initialize a new workflow session
     */
    public String init(Param... params) {
        return repository.insert(Session.of(params)).getSessionId();
    }

    /**
     * Execute an instance of {@link Workflow} form its current {@link Task} with the given params
     *
     */
    public ExecutionResult execute(String sessionId, Param ...params) {

        // Get Session
        val session = repository.get(sessionId);

        // Can be executed?
        if (!session.isExecutable()) {
            throw new SessionCantBeExecuted(sessionId);
        }

        // Get current Task
        val taskId = session.getLastExecution().map(Execution::getTaskId).getOrElse(initialTask.getId());
        val currentTask = tasks().find(task -> task.getId().equals(taskId)).getOrElseThrow(() -> new TaskNotFound(taskId));

        // Create Execution Context
        val executionContext = executionContextFactory.create(session, params);

        // Execute
        return execute(currentTask, session, executionContext);

    }

    private ExecutionResult execute(Task task, Session session, ExecutionContext executionContext) {

        System.out.println("EXECUTING " + task.getId());

        // Set the session as running
        val currentSession = repository.update(session.running(task, executionContext));

        // Execute the current task
        val taskResult = task.execute(executionContext);

        return Match(taskResult).of(

                Case($Continue($()), nextTask -> execute(nextTask, currentSession, executionContext)),

                Case($Block, () -> ExecutionResult.of(repository.update(currentSession.blocked(task)), task)),

                Case($Finish, () -> ExecutionResult.of(repository.update(currentSession.finished(task)), task)),

                Case($OnError($()), cause -> {
                    throw new ExecutionError(cause, repository.update(currentSession.onError(task, cause)), task);
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
