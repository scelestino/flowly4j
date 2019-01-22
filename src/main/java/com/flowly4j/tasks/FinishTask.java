package com.flowly4j.tasks;


import com.flowly4j.variables.Variables;
import com.flowly4j.tasks.results.Finish;
import com.flowly4j.tasks.results.TaskResult;
import io.vavr.collection.List;

/**
 * An instance of this {@link Task} is need be to used to finish a workflow execution.
 *
 * It is possible to configure multiple {@link FinishTask} inside the same workflow.
 *
 * Once an execution reach this kind of {@link Task}, the workflow instance where it is used will finish.
 *
 */
public abstract class FinishTask extends Task {

    @Override
    public TaskResult execute(String sessionId, Variables variables) {
        return new Finish();
    }

    @Override
    public List<Task> followedBy() {
        return List.Nil.instance();
    }

}
