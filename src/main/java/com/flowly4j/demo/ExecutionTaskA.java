package com.flowly4j.demo;

import com.flowly4j.tasks.ExecutionTask;
import com.flowly4j.tasks.Task;
import com.flowly4j.variables.Variables;

import static com.flowly4j.demo.CustomKeys.KEY2;

public class ExecutionTaskA extends ExecutionTask {

    @Override
    public Task next() {
        return new ExecutionTaskB();
    }

    @Override
    protected Variables perform(String sessionId, Variables variables) {
        return variables.set(KEY2, 123);
    }

}