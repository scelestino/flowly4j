package com.flowly4j.example;

import lombok.*;

@Value(staticConstructor = "of")
public class Person {
    String name;
    Integer age;
}

