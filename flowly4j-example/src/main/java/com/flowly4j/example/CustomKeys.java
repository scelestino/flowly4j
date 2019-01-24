package com.flowly4j.example;

import com.flowly4j.core.context.Key;
import com.flowly4j.core.context.Keys;

public class CustomKeys {

    public static Key<String> KEY1 = Keys.create("KEY1");
    public static Key<Integer> KEY2 = Keys.create("KEY2");
    public static Key<Boolean> KEY3 = Keys.create("KEY3");
    public static Key<Person> KEY4 = Keys.create("KEY4");

}
