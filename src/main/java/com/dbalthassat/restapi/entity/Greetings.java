package com.dbalthassat.restapi.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity
@SequenceGenerator(name = "SG", sequenceName = "SEQ_GREETINGS", allocationSize = 1)
public class Greetings extends  ApiEntity {
    @NotNull(message = "Ce champ ne peut pas être vide")
    private String name;

    private String description;

    @SuppressWarnings("unused")
    public Greetings() {
    }

    public Greetings(String name) {
        this(name, null);
    }

    public Greetings(String name, String description) {
        this.name = name;
        this.description = description;
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