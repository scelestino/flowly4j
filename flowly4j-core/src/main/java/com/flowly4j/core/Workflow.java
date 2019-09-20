package com.flowly4j.core;

import com.flowly4j.core.errors.*;
import com.flowly4j.core.context.ExecutionContext.ExecutionContextFactory;
import com.flowly4j.core.events.EventListener;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.output.ExecutionResult;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Execution;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.tasks.Task;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;

import static com.flowly4j.core.tasks.results.TaskResultPatterns.*;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;


@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class Workflow {

    Task initialTask;
    Repository repository;
    ExecutionContextFactory executionContextFactory;
    List<EventListener> eventListeners;

    public Workflow(Task initialTask, Repository repository, ExecutionContextFactory executionContextFactory, List<EventListener> eventListeners) {
        this.initialTask = initialTask;
        this.repository = repository;
        this.executionContextFactory = executionContextFactory;
        this.eventListeners = eventListeners;
        checkConsistency();
    }

    /**
     * Initialize a new workflow session
     */
    public String init(Param... params) {

        // Are params allowed?
        val keys = List.of(params).map(Param::getKey);
        if(!initialTask.accept(keys)) {
            throw new ParamsNotAllowedException(initialTask.getId(), keys, "Task " + initialTask + " doesn't accept one or more of the following keys " + keys.map(Key::getIdentifier));
        }

        val sessionId = repository.insert(Session.of(params)).getSessionId();

        // On Init Event
        eventListeners.forEach( l -> l.onInitialization(sessionId, List.of(params)) );

        return sessionId;

    }

    /**
     * Execute an instance of {@link Workflow} form its current {@link Task} with the given params
     *
     */
    public ExecutionResult execute(String sessionId, Param ...params) {
        return execute(sessionId, List.of(params));
    }

    /**
     * Execute an instance of {@link Workflow} form its current {@link Task} with the given params
     *
     */
    public ExecutionResult execute(String sessionId, List<Param> params) {

        // Get Session
        val session = repository.get(sessionId).getOrElseThrow( () -> new SessionNotFoundException(sessionId) );

        // Can be executed?
        if (!session.isExecutable()) {
            throw new SessionCantBeExecutedException(sessionId, "Session " + sessionId + " can't be executed");
        }

        // Get current Task
        val taskId = session.getLastExecution().map(Execution::getTaskId).getOrElse(initialTask.getId());
        val currentTask = getTasks().find(task -> task.getId().equals(taskId)).getOrElseThrow(() -> new TaskNotFoundException(taskId, "Task " + taskId + " doesn't belong to Session " + sessionId));

        // Are params allowed?
        val keys = params.map(Param::getKey);
        if(!currentTask.accept(keys)) {
            throw new ParamsNotAllowedException(taskId, keys, "Task " + currentTask + " doesn't accept one or more of the following keys " + keys.map(Key::getIdentifier));
        }

        // Set the session as running
        val runningSession = repository.update(session.resume(currentTask, params));

        eventListeners.forEach( l -> {

            // Create Execution Context
            val executionContext = executionContextFactory.create(runningSession);

            // On Start or Resume Event
            if(session.getLastExecution().isDefined()) {
                l.onResume(executionContext);
            } else {
                l.onStart(executionContext);
            }

        });

        // Execute
        return execute(currentTask, runningSession);

    }

    private ExecutionResult execute(Task task, Session session) {

        val executionContext = executionContextFactory.create(session);

        // Execute the current task
        return Match(task.execute(executionContext)).of(

                Case($Continue($(), $()), (nextTask, resultingExecutionContext) -> {

                    // Set the session as running (with new context and next task)
                    val runningSession = repository.update(session.continuee(nextTask, resultingExecutionContext));

                    // On Continue Event
                    eventListeners.forEach( l -> l.onContinue(resultingExecutionContext, task.getId(), nextTask.getId()) );

                    return execute(nextTask, runningSession);

                }),

                Case($SkipAndContinue($(), $()), (nextTask, resultingExecutionContext) -> {

                    // Set the session as running (with new context and next task)
                    val runningSession = repository.update(session.continuee(nextTask, resultingExecutionContext));

                    // On SkipAndContinue & Continue Event
                    eventListeners.forEach( l -> {
                        l.onSkip(resultingExecutionContext, task.getId());
                        l.onContinue(resultingExecutionContext, task.getId(), nextTask.getId());
                    });

                    return execute(nextTask, runningSession);


                }),

                Case($Block, () -> {

                    val blockedSession = repository.update(session.blocked(task));

                    // On Block Event
                    eventListeners.forEach( l -> l.onBlock(executionContext, task.getId()) );

                    return ExecutionResult.of(blockedSession, task, executionContext);

                }),

                Case($Finish, () -> {

                    val finishedSession = repository.update(session.finished(task));

                    // On Finish Event
                    eventListeners.forEach( l -> l.onFinish(executionContext, task.getId()) );

                    return ExecutionResult.of(finishedSession, task, executionContext);

                }),

                Case($ToRetry($(), $()), (cause, attempts) -> {

                    val sessionWithRetry = repository.update(session.toRetry(task, cause, attempts));

                    eventListeners.forEach( l -> l.onToRetry(executionContext, task.getId(), cause, attempts) );

                    throw new ExecutionException(sessionWithRetry, task, cause);

                }),

                Case($OnError($()), cause -> {

                    val sessionWithError = repository.update(session.onError(task, cause));

                    // On Error Event
                    eventListeners.forEach( l -> l.onError(executionContext, task.getId(), cause) );

                    throw new ExecutionException(sessionWithError, task, cause);

                })

        );

    }

    /**
     * It returns current allowed keys
     */
    public List<Key> currentAllowedKeys(String sessionId) {

        // Get Session
        val session = repository.get(sessionId).getOrElseThrow( () -> new SessionNotFoundException(sessionId) );

        // Get current Task
        val taskId = session.getLastExecution().map(Execution::getTaskId).getOrElse(initialTask.getId());
        val currentTask = getTasks().find(task -> task.getId().equals(taskId)).getOrElseThrow(() -> new TaskNotFoundException(taskId, "Task " + taskId + " doesn't belong to Session " + sessionId));

        return currentTask.allowedKeys();

    }

    /**
     * Get sessions to Retry
     */
    public Iterator<String> getToRetry() {
        return repository.getToRetry();
    }

    /**
     * It returns a list of every {@link Task} in this workflow
     *
     * @return list of {@link Task}
     */
    private List<Task> getTasks() {
        return getTasks(initialTask, List.empty());
    }
    private List<Task> getTasks(Task currentTask, List<Task> accum) {
        return accum.exists( t -> t.sameThan(currentTask) ) ? accum : currentTask.followedBy().foldRight(accum.append(currentTask), this::getTasks);
    }

    private void checkConsistency() {
        val tasks = getTasks().map(Task::getId);
        val duplicated = tasks.distinct().foldRight( tasks, (taskId, remainingTasks) -> remainingTasks.remove(taskId) );
        if(duplicated.nonEmpty()) {
            throw new IllegalStateException("There are repeated Tasks: " + duplicated + ". Workflow can't be constructed");
        }
    }

}
