package com.flowly4j.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    public static <T> T as(Object obj) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(obj), new TypeReference<T>() {});
        } catch(Exception ex) {
            return null;
        }

    }

}
