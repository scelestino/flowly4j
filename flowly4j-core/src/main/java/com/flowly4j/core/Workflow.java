package com.flowly4j.core;

import com.flowly4j.core.errors.*;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ExecutionContext.ExecutionContextFactory;
import com.flowly4j.core.events.EventListener;
import com.flowly4j.core.input.Key;
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
    protected List<EventListener> eventListeners = List.empty();

    /**
     * Initialize a new workflow session
     */
    public String init(Param... params) {

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
        List<Key> keys = params.map(Param::getKey);
        if(!currentTask.accept(keys)) {
            throw new ParamsNotAllowedException(taskId, keys, "Task " + currentTask + " doesn't accept one or more of the following keys " + keys.map(Key::getIdentifier));
        }

        // Create Execution Context
        val executionContext = executionContextFactory.create(session, params);

        // Set the session as running
        val runningSession = repository.update(session.running(currentTask, executionContext));

        // On Start Event
        if(currentTask.equals(initialTask)) {
            eventListeners.forEach( l -> l.onStart(executionContext) );
        }

        // Execute
        return execute(currentTask, runningSession, executionContext);

    }

    private ExecutionResult execute(Task task, Session session, ExecutionContext executionContext) {

        // Execute the current task
        val taskResult = task.execute(executionContext);

        return Match(taskResult).of(

                Case($Continue($()), nextTask -> {

                    // Set the session as running (with new context and next task)
                    val runningSession = repository.update(session.running(nextTask, executionContext));

                    // On Continue Event
                    eventListeners.forEach( l -> l.onContinue(executionContext, task.getId(), nextTask.getId()) );

                    return execute(nextTask, runningSession, executionContext);

                }),

                Case($SkipAndContinue($()), nextTask -> {

                    // Set the session as running (with new context and next task)
                    val runningSession = repository.update(session.running(nextTask, executionContext));

                    // On SkipAndContinue & Continue Event
                    eventListeners.forEach( l -> {
                        l.onSkip(executionContext, task.getId());
                        l.onContinue(executionContext, task.getId(), nextTask.getId());
                    });

                    return execute(nextTask, runningSession, executionContext);


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

                Case($OnError($()), cause -> {

                    // On Error Event
                    eventListeners.forEach( l -> l.onError(executionContext, task.getId(), cause) );

                    throw new ExecutionException(repository.update(session.onError(task, cause)), task, cause);

                })

        );

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
        return accum.contains(currentTask) ? accum : currentTask.followedBy().foldRight(accum.append(currentTask), this::getTasks);
    }

}
