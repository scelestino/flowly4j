package com.flowly4j.examplemariadb;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.events.EventListener;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.session.Attempts;
import io.vavr.collection.List;


public class ConsoleEventListener implements EventListener {

    @Override
    public void onInitialization(String sessionId, List<Param> params) {
        System.out.println("Session " + sessionId + " initialized with " + params);
    }

    @Override
    public void onStart(ReadableExecutionContext executionContext) {
        System.out.println("Session " + executionContext.getSessionId() + " started");
    }

    @Override
    public void onResume(ReadableExecutionContext executionContext) {
        System.out.println("Session " + executionContext.getSessionId() + " resumed");
    }

    @Override
    public void onContinue(ReadableExecutionContext executionContext, String currentTask, String nextTask) {
        System.out.println("Session " + executionContext.getSessionId() + " continue from " + currentTask + " to " + nextTask);
    }

    @Override
    public void onSkip(ReadableExecutionContext executionContext, String currentTask) {
        System.out.println("Session " + executionContext.getSessionId() + " skip " + currentTask);
    }

    @Override
    public void onBlock(ReadableExecutionContext executionContext, String currentTask) {
        System.out.println("Session " + executionContext.getSessionId() + " blocked");
    }

    @Override
    public void onFinish(ReadableExecutionContext executionContext, String currentTask) {
        System.out.println("Session " + executionContext.getSessionId() + " finished");
    }

    @Override
    public void onError(ReadableExecutionContext executionContext, String currentTask, Throwable cause) {
        System.out.println("Session " + executionContext.getSessionId() + " with task " + currentTask + " with error " + cause.getMessage());
    }

    @Override
    public void onToRetry(ReadableExecutionContext executionContext, String currentTask, Throwable cause, Attempts attempts) {
        System.out.println("Session " + executionContext.getSessionId() + " with task " + currentTask + " with error " + cause.getMessage() + " and retry");
    }

}
