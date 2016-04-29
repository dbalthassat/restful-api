package com.dbalthassat.restapi.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@SequenceGenerator(name = "SG", sequenceName = "SEQ_GREETINGS", allocationSize = 1)
public class Greetings extends ApiEntity {
    @NotNull(message = "Ce champ ne peut pas être vide")
    private String name;

    private String description;

    @OneToMany(cascade = ALL, fetch = FetchType.LAZY, mappedBy = "greetings")
    private final List<Messages> messages;

    public Greetings() {
        this(null);
    }

    public Greetings(String name) {
        this(name, null);
    }

    public Greetings(String name, String description) {
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

    public List<Messages> getMessages() {
        return messages;
    }

    public void addMessage(Messages message) {
        this.messages.add(message);
        message.setGreetings(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Greetings greetings = (Greetings) o;
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