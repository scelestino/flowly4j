package com.flowly4j.core.tasks.results;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.session.Attempts;
import com.flowly4j.core.tasks.Task;
import io.vavr.Tuple;
import io.vavr.Tuple0;
import io.vavr.Tuple1;
import io.vavr.Tuple2;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

/**
 * Interface of a {@link com.flowly4j.core.tasks.Task} execution result
 */
@Patterns
public interface TaskResult {

    @Unapply
    static Tuple2<Task, ExecutionContext> Continue(Continue result) {
        return Tuple.of(result.nextTask, result.executionContext);
    }

    @Unapply
    static Tuple2<Task, ExecutionContext> SkipAndContinue(SkipAndContinue result) {
        return Tuple.of(result.nextTask, result.executionContext);
    }

    @Unapply
    static Tuple1<Throwable> OnError(OnError result) {
        return Tuple.of(result.cause);
    }

    @Unapply
    static Tuple0 Block(Block result) {
        return Tuple.empty();
    }

    @Unapply
    static Tuple0 Finish(Finish result) {
        return Tuple.empty();
    }

    @Unapply
    static Tuple2<Throwable, Attempts> ToRetry(ToRetry result) { return Tuple.of(result.cause, result.attempts); }

}