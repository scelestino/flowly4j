package com.flowly4j.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cancellation {
    private String reason;
    private DateTime at;
}
