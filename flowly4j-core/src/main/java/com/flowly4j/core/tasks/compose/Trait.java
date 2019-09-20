package com.flowly4j.core.tasks.compose;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;

/**
 * Objects from this interfaces work as an Task aspect
 */
public interface Trait {

    /**
     * Decorate execution
     */
    Function1<ExecutionContext, TaskResult> compose(Function1<ExecutionContext, TaskResult> next);

    /**
     * Add new keys to the Task
     */
    List<Key> allowedKeys();

    /**
     * Add new children to the Task
     */
    List<Task> followedBy();

    /**
     * Order used to execute all the traits in a Task (the lower the number, the first it execute)
     */
    Integer order();

}
