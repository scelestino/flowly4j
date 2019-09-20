package com.flowly4j.core.events;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.session.Attempts;
import io.vavr.collection.List;

public interface EventListener {

    void onInitialization(String sessionId, List<Param> params);

    void onStart(ReadableExecutionContext executionContext);

    void onResume(ReadableExecutionContext executionContext);

    void onContinue(ReadableExecutionContext executionContext, String currentTask, String nextTask);

    void onSkip(ReadableExecutionContext executionContext, String currentTask);

    void onBlock(ReadableExecutionContext executionContext, String currentTask);

    void onFinish(ReadableExecutionContext executionContext, String currentTask);

    void onError(ReadableExecutionContext executionContext, String currentTask, Throwable cause);

    void onToRetry(ReadableExecutionContext executionContext, String currentTask, Throwable cause, Attempts attempts);

}
