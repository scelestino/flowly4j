package com.flowly4j.example;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;

import static com.flowly4j.example.CustomKeys.KEY2;

public class ExecutionTaskA extends ExecutionTask {

    @Override
    public Task next() {
        return new ExecutionTaskB();
    }

    @Override
    protected void perform(ExecutionContext executionContext) {

    }

}