package com.flowly4j.mongodb.model;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Execution {
    private String taskId;
    private DateTime at;
    private Option<String> message;
}
