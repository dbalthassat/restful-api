package com.dbalthassat.restapi.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Params<T> {
    private final List<Predicate<T>> filters;
    private final Optional<String> query;

    public Params(List<Predicate<T>> filters, Optional<String> query) {
        this.filters = filters;
        this.query = query;
    }

    public List<Predicate<T>> getFilters() {
        return filters;
    }

    public Optional<String> getQuery() {
        return query;
    }
}
