package com.flowly4j.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class SessionMixIn {

    @JsonIgnore
    public abstract Boolean isExecutable();

}
