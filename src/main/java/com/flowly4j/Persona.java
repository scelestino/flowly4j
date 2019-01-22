package com.flowly4j;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
class Persona {
    private String name;
    private Integer age;
    private List<String> bla = new ArrayList<>();

    public Persona() {
    }

    public Persona(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public List<String> getBla() {
        return bla;
    }

    public void setBla(List<String> bla) {
        this.bla = bla;
    }
}
