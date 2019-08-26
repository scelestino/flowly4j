package com.flowly4j.core.tasks.compose;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;

public interface Trait {
    Function1<ExecutionContext, TaskResult> compose(Function1<ExecutionContext, TaskResult> next);
    List<Key> allowedKeys();
    Integer order();
}
