package com.dbalthassat.quizrc.entity;

public class Greeting implements Searchable {
    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String queryParameter() {
        return content;
    }
}