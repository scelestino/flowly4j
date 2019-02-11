package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;

public abstract class ForkTask extends Task {

    public ForkTask(String id) {
        super(id);
    }

    protected abstract List<Branch> branches();

    @Override
    public TaskResult execute(ExecutionContext executionContext) {
        try {

        } catch (Throwable throwable) {
            return OnError(throwable);
        }
    }

    @Override
    public List<Task> followedBy() {
        return null;
    }

    @Override
    protected List<Key> allowedKeys() {
        return null;
    }

}
