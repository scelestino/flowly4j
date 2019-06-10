package com.flowly4j.example;

import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;
import io.vavr.collection.List;

import java.time.Instant;

import static com.flowly4j.example.CustomKeys.*;

public class ExecutionTaskA extends ExecutionTask {

    public ExecutionTaskA() {
        super("ExecutionTaskA");
    }

    @Override
    public Task next() {
        return new ExecutionTaskB();
    }

    @Override
    protected void perform(WritableExecutionContext executionContext) {
        executionContext.set(KEY1, "esta es una prueba");
        executionContext.set(KEY2, 123);
        //executionContext.set(KEY6, Instant.now());
        executionContext.set(KEY4, Person.of("juan", 123, Instant.now()));
    }

    @Override
    public List<Key> allowedKeys() {
        return List.of(KEY2, KEY1, KEY6);
    }

}