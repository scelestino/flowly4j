package com.flowly4j.core.tasks.compose.retry;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.session.Attempts;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.compose.retry.scheduling.SchedulingStrategy;
import com.flowly4j.core.tasks.compose.retry.stopping.StoppingStrategy;
import com.flowly4j.core.tasks.results.TaskResult;
import com.flowly4j.core.tasks.results.ToRetry;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.val;

import java.time.Instant;

import static com.flowly4j.core.tasks.compose.retry.stopping.Retryable.$Retryable;
import static com.flowly4j.core.tasks.results.TaskResultPatterns.$OnError;
import static io.vavr.API.*;

public class Retry implements Trait {

    private SchedulingStrategy schedulingStrategy;
    private StoppingStrategy stoppingStrategy;

    private Retry(SchedulingStrategy schedulingStrategy, StoppingStrategy stoppingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
        this.stoppingStrategy = stoppingStrategy;
    }

    @Override
    public Function1<ExecutionContext, TaskResult> compose(Function1<ExecutionContext, TaskResult> next) {
        return context -> {

            val attempts = context.getAttempts().getOrElse( () -> new Attempts(1, Instant.now(), Option.none()) );

            return Match(next.apply(context)).of(
                    Case($OnError($Retryable($( r -> r.canBeRetried() && stoppingStrategy.shouldRetry(context, attempts) ))), cause -> {
                        return new ToRetry(cause, attempts.withNextRetry(schedulingStrategy.nextRetry(context, attempts)));
                    }),
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
        return List.empty();
    }

    @Override
    public Integer order() {
        return 100;
    }

    public static <T extends Task> Function1<T, Trait> of(SchedulingStrategy schedulingStrategy, StoppingStrategy stoppingStrategy) {
        return parent -> new Retry(schedulingStrategy, stoppingStrategy);
    }

}
