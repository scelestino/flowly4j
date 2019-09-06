package com.flowly4j.core.tasks.compose.alternative;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;

import static com.flowly4j.core.tasks.results.TaskResultPatterns.$OnError;
import static io.vavr.API.*;

public class Alternative implements Trait {

    private Task nextOnError;

    private Alternative(Task nextOnError) {
        this.nextOnError = nextOnError;
    }

    @Override
    public Function1<ExecutionContext, TaskResult> compose(Function1<ExecutionContext, TaskResult> next) {
        return executionContext -> {
            return Match(next.apply(executionContext)).of(
                    Case($OnError($()), cause -> new Continue(nextOnError)),
                    Case($(), otherwise -> otherwise)
            );
        };
    }

    @Override
    public List<Key> allowedKeys() {
        return List.empty();
    }

    @Override
    public List<Task> followedBy() {
        return List.of(nextOnError);
    }

    @Override
    public Integer order() {
        return 0;
    }

    public static <T extends Task> Function1<T, Trait> of(Task nextOnError) {
        return parent -> new Alternative(nextOnError);
    }

}
