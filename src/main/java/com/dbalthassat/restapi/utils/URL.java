package com.dbalthassat.restapi.utils;

public class URL {
    private final String URL;
    private final int countParams;

    public URL(String URL, int countParams) {
        this.URL = URL;
        this.countParams = countParams;
    }

    public String getURL() {
        return URL;
    }

    public int getCountParams() {
        return countParams;
    }
}
