package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;

import java.util.Objects;

/**
 * Task is something to do inside a workflow
 *
 * There is no possible to use two identical Task in the same workflow
 *
 */
public abstract class Task {

    public String getId() {
        return this.getClass().getSimpleName();
    }

    public abstract TaskResult execute(ExecutionContext executionContext);

    public abstract List<Task> followedBy();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
