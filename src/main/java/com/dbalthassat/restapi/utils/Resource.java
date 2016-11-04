package com.dbalthassat.restapi.utils;

public class Resource {
    private final String resource;
    private final Long id;

    public Resource(String resource, Long id) {
        this.resource = resource;
        this.id = id;
    }

    public Resource(String resource) {
        this(resource, null);
    }

    public boolean hasSpecificId() {
        return id != null;
    }

    public String getResource() {
        return resource;
    }

    public Long getId() {
        return id;
    }
}
