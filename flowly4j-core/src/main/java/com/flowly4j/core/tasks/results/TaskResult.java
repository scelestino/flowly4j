package com.flowly4j.core.tasks.results;

import com.flowly4j.core.tasks.Task;
import io.vavr.Tuple;
import io.vavr.Tuple0;
import io.vavr.Tuple1;
import io.vavr.match.annotation.Unapply;

/**
 * Interface of a {@link com.flowly4j.core.tasks.Task} execution result
 */
@io.vavr.match.annotation.Patterns
public interface TaskResult {

    @Unapply
    static Tuple1<Task> Continue(Continue result) {
        return Tuple.of(result.nextTask);
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

}