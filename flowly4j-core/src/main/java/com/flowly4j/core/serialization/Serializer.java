package com.flowly4j.core.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.errors.SerializationException;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class Serializer {

    private ObjectMapper objectMapper;

    public String write(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Throwable cause) {
            throw new SerializationException("Error trying to serialize " + obj, cause);
        }
    }

    public <T> T read(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException cause) {
            throw new SerializationException("Error trying to deserialize " + json, cause);
        }
    }

    public <T> T deepCopy(Object obj, TypeReference<T> typeReference) {
        return read(write(obj), typeReference);
    }

}
