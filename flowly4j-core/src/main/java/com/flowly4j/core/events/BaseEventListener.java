package com.flowly4j.core.events;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Param;
import io.vavr.collection.List;

public abstract class BaseEventListener implements EventListener {

    @Override
    public void onInitialization(String sessionId, List<Param> params) {
    }

    @Override
    public void onStart(ReadableExecutionContext executionContext) {
    }

    @Override
    public void onContinue(ReadableExecutionContext executionContext, String currentTask, String nextTask) {
    }

    @Override
    public void onSkip(ReadableExecutionContext executionContext, String currentTask) {
    }

    @Override
    public void onBlock(ReadableExecutionContext executionContext, String currentTask) {
    }

    @Override
    public void onFinish(ReadableExecutionContext executionContext, String currentTask) {
    }

    @Override
    public void onError(ReadableExecutionContext executionContext, String currentTask, Throwable cause) {
    }

}
