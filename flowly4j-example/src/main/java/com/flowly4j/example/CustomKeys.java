package com.flowly4j.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flowly4j.core.input.Key;
import io.vavr.collection.Map;

public class CustomKeys {

    public static Key<String> KEY1 = Key.of("KEY1", new TypeReference<String>() {});
    public static Key<Integer> KEY2 = Key.of("KEY2", new TypeReference<Integer>() {});
    public static Key<Boolean> KEY3 = Key.of("KEY3", new TypeReference<Boolean>() {});
    public static Key<Person> KEY4 = Key.of("KEY4", new TypeReference<Person>() {});
    public static Key<Map<String, Integer>> KEY5 = Key.of("KEY5", new TypeReference<Map<String, Integer>>() {});

}
