package com.flowly4j.core.errors;

import com.flowly4j.core.input.Key;
import io.vavr.collection.List;
import lombok.Getter;

@Getter
public class ParamsNotAllowedException extends RuntimeException {

    private String taskId;
    private List<Key> keys;

    public ParamsNotAllowedException(String taskId, List<Key> keys, String message) {
        super(message);
        this.taskId = taskId;
        this.keys = keys;
    }

}
