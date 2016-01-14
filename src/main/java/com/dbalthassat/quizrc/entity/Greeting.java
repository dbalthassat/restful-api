package com.dbalthassat.quizrc.entity;

public class Greeting implements Entity {
    private long id;
    private String name;

    @SuppressWarnings("unused")
    public Greeting() {

    }

    public Greeting(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String queryValue() {
        return name;
    }
}