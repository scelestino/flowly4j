package com.flowly4j.core.tasks;


import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.Finish;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function2;
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

    public FinishTask() {
    }

    public FinishTask(String id) {
        super(id);
    }

    @Override
    public List<Task> followedBy() {
        return List.empty();
    }

    @Override
    public List<Key> allowedKeys() {
        return List.empty();
    }

    @Override
    public TaskResult execute(ExecutionContext executionContext) {
        return new Finish();
    }

}
