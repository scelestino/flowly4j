package com.flowly4j.demo;

import com.flowly4j.tasks.DisjunctionTask;
import io.vavr.collection.List;

public class DisjunctionA extends DisjunctionTask {

    public DisjunctionA() {
        super(new BlockingA(), new FinishA(), v -> true);
    }

}