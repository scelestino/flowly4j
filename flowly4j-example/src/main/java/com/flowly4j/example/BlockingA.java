package com.flowly4j.example;

import com.flowly4j.core.tasks.BlockingTask;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.variables.ReadableVariables;

import static com.flowly4j.example.CustomKeys.KEY2;

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