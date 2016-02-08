package com.dbalthassat.restapi.entity;

import javax.validation.constraints.NotNull;

public class Greeting implements Entity {
    @NotNull
    private long id;

    @NotNull
    private String name;

    private String description;

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

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String queryValue() {
        return name + '#' + description;
    }
}