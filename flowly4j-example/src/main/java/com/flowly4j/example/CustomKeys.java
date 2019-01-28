package com.flowly4j.example;

import com.flowly4j.core.input.Key;
import io.vavr.collection.Map;

public class CustomKeys {

    public static Key<String> KEY1 = Key.of("KEY1");
    public static Key<Integer> KEY2 = Key.of("KEY2");
    public static Key<Boolean> KEY3 = Key.of("KEY3");
    public static Key<Person> KEY4 = Key.of("KEY4");
    public static Key<Map<String, Integer>> KEY5 = Key.of("KEY5");

}
