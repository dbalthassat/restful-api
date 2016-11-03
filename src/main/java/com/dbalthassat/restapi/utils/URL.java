package com.dbalthassat.restapi.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class URL {
    public static class Param {

        private final String key;
        private final String value;
        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

    private final String URL;
    private final List<Param> params;

    public URL(String URL, List<Param> params) {
        this.URL = URL;
        this.params = new LinkedList<>(params);
    }

    public URL(String url) {
        this(url, new LinkedList<>());
    }

    public void addParam(Param param) {
        params.add(param);
    }

    public void removeParam(String key) {
        params.removeIf(p -> p.key.equals(key));
    }

    public String getURL() {
        return URL;
    }

    public int getCountParams() {
        return params.size();
    }

    public static URL buildUrl(String url, String queryString) {
        if(queryString != null) {
            String[] split = queryString.split("&");
            List<Param> params = Arrays.stream(split)
                    .map(e -> {
                        String[] keyAndValue = e.split("=");
                        return new Param(keyAndValue[0], keyAndValue[1]);
                    })
                    .collect(Collectors.toList());
            return new URL(url, params);
        }
        return new URL(url);
    }

    @Override
    public String toString() {
        String result = URL;
        if(!params.isEmpty()) {
            result += "?";
            StringJoiner joiner = new StringJoiner("&");
            params.forEach(e -> joiner.add(e.key + "=" + e.value));
            result += joiner.toString();
        }
        return result;
    }
}
