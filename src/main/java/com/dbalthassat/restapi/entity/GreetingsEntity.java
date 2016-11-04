package com.dbalthassat.restapi.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "greetings")
@SequenceGenerator(name = "SG", sequenceName = "SEQ_GREETINGS", allocationSize = 1)
public class GreetingsEntity extends ApiEntity {
    @NotNull(message = "Ce champ ne peut pas être vide")
    private String name;

    private String description;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = ALL, fetch = FetchType.EAGER)
    private final List<MessagesEntity> messages;

    public GreetingsEntity() {
        this(null);
    }

    public GreetingsEntity(String name) {
        this(name, null);
    }

    public GreetingsEntity(String name, String description) {
        this.name = name;
        this.description = description;
        this.messages = new LinkedList<>();
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

    public List<MessagesEntity> getMessages() {
        return messages;
    }

    public void addMessage(MessagesEntity message) {
        this.messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GreetingsEntity greetings = (GreetingsEntity) o;
        return name != null
                    ? name.equals(greetings.name)
                    : greetings.name == null
                && (description != null
                    ? description.equals(greetings.description)
                    : greetings.description == null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}