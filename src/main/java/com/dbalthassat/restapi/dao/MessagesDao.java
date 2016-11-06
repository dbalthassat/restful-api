package com.dbalthassat.restapi.dao;

public class MessagesDao extends ApiDao {
    private String significance;
    private String value;

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
