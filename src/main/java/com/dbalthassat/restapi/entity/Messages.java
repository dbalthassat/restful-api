package com.dbalthassat.restapi.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity
@SequenceGenerator(name = "SG", sequenceName = "SEQ_MESSAGES", allocationSize = 1)
public class Messages extends ApiEntity {
    @NotNull(message = "Ce champ ne peut pas être vide")
    private String value;

    @NotNull(message = "Ce champ ne peut pas être vide")
    private String significance;

    public Messages() {
    }

    public Messages(String significance, String value) {
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
}
