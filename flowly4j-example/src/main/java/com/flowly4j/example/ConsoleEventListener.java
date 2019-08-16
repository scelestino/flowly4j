package com.flowly4j.example;

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
    public void onStart(String sessionId, ReadableExecutionContext executionContext) {
        System.out.println("Session " + sessionId + " started");
    }

    @Override
    public void onResume(String sessionId, ReadableExecutionContext executionContext) {
        System.out.println("Session " + sessionId + " resumed");
    }

    @Override
    public void onContinue(String sessionId, ReadableExecutionContext executionContext, String currentTask, String nextTask) {
        System.out.println("Session " + sessionId + " continue from " + currentTask + " to " + nextTask);
    }

    @Override
    public void onSkip(String sessionId, ReadableExecutionContext executionContext, String currentTask) {
        System.out.println("Session " + sessionId + " skip " + currentTask);
    }

    @Override
    public void onBlock(String sessionId, ReadableExecutionContext executionContext, String currentTask) {
        System.out.println("Session " + sessionId + " blocked");
    }

    @Override
    public void onFinish(String sessionId, ReadableExecutionContext executionContext, String currentTask) {
        System.out.println("Session " + sessionId + " finished");
    }

    @Override
    public void onError(String sessionId, ReadableExecutionContext executionContext, String currentTask, Throwable cause) {
        System.out.println("Session " + sessionId + " with task " + currentTask + " with error " + cause.getMessage());
    }

    @Override
    public void onToRetry(String sessionId, ReadableExecutionContext executionContext, String currentTask, Throwable cause, Attempts attempts) {
        System.out.println("Session " + sessionId + " with task " + currentTask + " with error " + cause.getMessage() + " and retry");
    }

}
