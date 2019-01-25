package com.flowly4j.mongodb.model;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private ObjectId _id;
    private String sessionId;
    private Map<String, Object> variables;
    private Option<Execution> lastExecution;
    private Option<Cancellation> cancellation;
    private DateTime createAt;
    private Status status;
    private Long version;
}
