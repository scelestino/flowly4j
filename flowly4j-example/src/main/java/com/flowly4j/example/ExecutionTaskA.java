package com.flowly4j.example;

import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.ForkJoinTask;
import com.flowly4j.core.tasks.Task;
import io.vavr.collection.List;

import static com.flowly4j.example.CustomKeys.*;
import static io.vavr.API.For;

public class ExecutionTaskA extends ExecutionTask {

    public ExecutionTaskA() {
        super("ExecutionTaskA");
    }

    @Override
    public Task next() {
        return new ForkJoinTask("ForkJoinTask", new ExecutionTaskB());
    }

    @Override
    protected void perform(WritableExecutionContext executionContext) {
        executionContext.set(KEY1, "esta es una prueba");
        executionContext.set(KEY2, 123);
        executionContext.set(KEY4, Person.of("juan", 123));
    }

    @Override
    protected List<Key> allowedKeys() {
        return List.of(KEY3);
    }

}