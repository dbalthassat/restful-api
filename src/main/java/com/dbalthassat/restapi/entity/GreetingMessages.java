package com.dbalthassat.restapi.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "SG", sequenceName = "SEQ_GREETINGMESSAGES", allocationSize = 1)
public class GreetingMessages extends ApiEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private Greetings greetings;

    @ManyToOne(cascade = CascadeType.ALL)
    private Messages messages;

    public GreetingMessages() {
    }

    public GreetingMessages(Greetings greetings, Messages messages) {
        this.greetings = greetings;
        this.messages = messages;
    }

    public Greetings getGreetings() {
        return greetings;
    }

    public void setGreetings(Greetings greetings) {
        this.greetings = greetings;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}
