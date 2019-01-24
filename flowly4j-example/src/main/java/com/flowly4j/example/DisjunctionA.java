package com.flowly4j.example;

import com.flowly4j.core.tasks.DisjunctionTask;

public class DisjunctionA extends DisjunctionTask {

    public DisjunctionA() {
        super(new BlockingA(), new FinishA(), v -> true);
    }

}