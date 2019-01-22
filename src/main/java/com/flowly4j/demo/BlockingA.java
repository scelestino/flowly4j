package com.flowly4j.demo;

import com.flowly4j.tasks.BlockingTask;
import com.flowly4j.tasks.Task;
import com.flowly4j.variables.ReadableVariables;

import static com.flowly4j.demo.CustomKeys.KEY2;

public class BlockingA extends BlockingTask {

    @Override
    public Boolean condition(ReadableVariables variables) {
        return variables.contains(KEY2);
    }

    @Override
    public Task next() {
        return new FinishA();
    }

}