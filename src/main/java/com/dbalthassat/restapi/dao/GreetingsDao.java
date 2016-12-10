package com.dbalthassat.restapi.dao;

import java.util.List;

public class GreetingsDao extends ApiDao {
    private String name;
    private String description;
    private List<MessagesDao> messages;

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

    public List<MessagesDao> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesDao> messages) {
        this.messages = messages;
    }
}
