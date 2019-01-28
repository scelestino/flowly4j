package com.flowly4j.core.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.errors.SerializationException;


public class JsonSerializer implements Serializer<String> {

    private ObjectMapper objectMapper;

    public JsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String write(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Throwable cause) {
            throw new SerializationException("Error trying to serialize " + obj, cause);
        }
    }

    @Override
    public <T> T read(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<T>(){});
        } catch (Throwable cause) {
            throw new SerializationException("Error trying to deserialize " + data, cause);
        }
    }

    @Override
    public <T> T deepCopy(Object obj) {
        return read(write(obj));
    }

}
