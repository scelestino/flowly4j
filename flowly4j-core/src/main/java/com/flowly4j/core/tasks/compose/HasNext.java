package com.flowly4j.core.tasks.compose;

import com.flowly4j.core.tasks.Task;

/**
 * Task followed by one Task
 */
public interface HasNext {
    Task next();
}
