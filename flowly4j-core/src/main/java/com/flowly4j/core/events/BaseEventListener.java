package com.flowly4j.core.events;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.session.Attempts;
import io.vavr.collection.List;

public abstract class BaseEventListener implements EventListener {

    @Override
    public void onInitialization(String sessionId, List<Param> params) {
    }

    @Override
    public void onStart(String sessionId, ReadableExecutionContext executionContext) {
    }

    @Override
    public void onResume(String sessionId, ReadableExecutionContext executionContext) {
    }

    @Override
    public void onContinue(String sessionId, ReadableExecutionContext executionContext, String currentTask, String nextTask) {
    }

    @Override
    public void onSkip(String sessionId, ReadableExecutionContext executionContext, String currentTask) {
    }

    @Override
    public void onBlock(String sessionId, ReadableExecutionContext executionContext, String currentTask) {
    }

    @Override
    public void onFinish(String sessionId, ReadableExecutionContext executionContext, String currentTask) {
    }

    @Override
    public void onError(String sessionId, ReadableExecutionContext executionContext, String currentTask, Throwable cause) {
    }

    @Override
    public void onToRetry(String sessionId, ReadableExecutionContext executionContext, String currentTask, Throwable cause, Attempts attempts) {
    }

}
