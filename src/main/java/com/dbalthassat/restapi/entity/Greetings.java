package com.dbalthassat.restapi.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity
@SequenceGenerator(name = "SG", sequenceName = "SEQ_GREETINGS", allocationSize = 1)
public class Greetings extends  ApiEntity {
    @NotNull
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
}