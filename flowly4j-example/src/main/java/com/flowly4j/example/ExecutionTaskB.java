package com.flowly4j.example;

import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import static com.flowly4j.example.CustomKeys.*;

public class ExecutionTaskB extends ExecutionTask {

    public ExecutionTaskB() {
        super("ExecutionTaskB");
    }

    @Override
    public Task next() {
        return new DisjunctionA();
    }

    @Override
    protected void perform(WritableExecutionContext executionContext) {
        System.out.println(executionContext.get(KEY1));
        executionContext.set(KEY4, Person.of("juan", 20));
        executionContext.unset(KEY2);
        Person x = executionContext.get(KEY4).get();

        System.out.println(x);
    }

}