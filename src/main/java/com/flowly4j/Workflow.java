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

    public Either<Throwable, String> init(/* params */) {
        // TODO: init session with params and return session id
        return Either.right("123");
    }

    public Either<Throwable, ExecutionResult> execute(String sessionId /* params */) {

        // Get Session, can be executed?
        return getSession(sessionId).filterOrElse(Session::isExecutable, session -> new SessionCantBeExecuted(sessionId)).flatMap(session -> {

            // Get current Task
            String taskId = session.lastExecution.map(Execution::taskId).getOrElse(initialTask.id());

            // Get the task to execute
            return task(taskId).flatMap( currentTask -> {
                // TODO: Merge params with variables
                return execute(currentTask, session, session.variables);
            });

        });

    }

    private Either<Throwable, ExecutionResult> execute(Task task, Session session, Variables variables) {

        Consumer<Throwable> onFailure = cause -> Either.left(new ExecutionError(cause, session, task));

        Consumer<Session> onSuccess = s -> {
            Either.right(new ExecutionResult(s.id, task.id(), variables, Status.FINISHED));
        };

        repository.save(session.running(task, variables)).fold(onFailure, onSuccess);

        System.out.println("EXECUTING... " + task.id());
        return Either.left(new RuntimeException("NOT IMPLEMENTED"));
//        return Either.right(new ExecutionResult(session.id, task.id(), variables, "STATUS"));
    }

    /// TODO: ir a buscar al repo
    private Either<Throwable, Session> getSession(String sessionId) {
        return Either.right(new Session("123", new Variables(HashMap.empty()), Option.of(new Execution("ExecutionTaskB")), Option.none(), DateTime.now(), "" ));
    }

    /**
     *
     *
     * @param taskId
     * @return
     */
    private Either<Throwable, Task> task(String taskId) {
        return tasks().find(task -> task.id().equals(taskId)).toEither(new TaskNotFound(taskId));
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
