package com.flowly4j;

import com.flowly4j.errors.SessionCantBeExecuted;
import com.flowly4j.repository.model.Execution;
import com.flowly4j.repository.model.WorkflowSession;
import com.flowly4j.tasks.Task;
import com.flowly4j.variables.Variables;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Either;

public class Workflow {

    private Task initialTask;

    public Either<Throwable, String> init(/* params */) {
        // TODO: init session with params and return session id
        return Either.right("123");
    }

    public Either<Throwable, ExecutionResult> execute(String sessionId /* params */) {

        // Get Session, can be executed?
        return getSession(sessionId).filterOrElse(WorkflowSession::isExecutable, session -> new SessionCantBeExecuted(sessionId)).flatMap( session -> {

            String taskId = session.lastExecution.map(Execution::taskId).getOrElse(initialTask.id());



            return Either.left(new RuntimeException());

        });

    }

    /// TODO: ir a buscar al repo
    private Either<Throwable, WorkflowSession> getSession(String sessionId) {
        return Either.right(new WorkflowSession("123", new Variables(HashMap.empty())));
    }

  //  private List<Task> tasks() {
  //      List<Task> accum = List.empty();

 //   }
   // private List<Task> tasks(Task currentTask, List<Task> accum) {
     //   if(accum.contains(currentTask)) return accum;
       // else currentTask.followedBy().foldRight( accum.append(currentTask), tasks);
   // }

}
