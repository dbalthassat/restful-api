package com.dbalthassat.restapi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "messages")
@SequenceGenerator(name = "SG", sequenceName = "SEQ_MESSAGES", allocationSize = 1)
public class MessagesEntity extends GenericEntity {
    @NotNull(message = "Ce champ ne peut pas être vide")
    private String value;

    @NotNull(message = "Ce champ ne peut pas être vide")
    private String significance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private GreetingsEntity greetings;

    public MessagesEntity() {
    }

    public MessagesEntity(String significance, String value) {
        this.significance = significance;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }

    public GreetingsEntity getGreetings() {
        return greetings;
    }

    public void setGreetings(GreetingsEntity greetings) {
        this.greetings = greetings;
    }
}
