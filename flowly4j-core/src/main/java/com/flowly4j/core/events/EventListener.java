package com.flowly4j.core.events;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.session.Attempts;
import io.vavr.collection.List;

public interface EventListener {

    void onInitialization(String sessionId, List<Param> params);

    void onStart(String sessionId, ReadableExecutionContext executionContext);

    void onResume(String sessionId, ReadableExecutionContext executionContext);

    void onContinue(String sessionId, ReadableExecutionContext executionContext, String currentTask, String nextTask);

    void onSkip(String sessionId, ReadableExecutionContext executionContext, String currentTask);

    void onBlock(String sessionId, ReadableExecutionContext executionContext, String currentTask);

    void onFinish(String sessionId, ReadableExecutionContext executionContext, String currentTask);

    void onError(String sessionId, ReadableExecutionContext executionContext, String currentTask, Throwable cause);

    void onToRetry(String sessionId, ReadableExecutionContext executionContext, String currentTask, Throwable cause, Attempts attempts);

}
