package com.dbalthassat.restapi.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Params<T> {
    private final List<Predicate<T>> filters;
    private final List<Comparator<T>> sorts;
    private final Optional<String> query;

    public Params(List<Predicate<T>> filters, List<Comparator<T>> sorts, Optional<String> query) {
        this.filters = filters;
        this.query = query;
        this.sorts = sorts;
    }

    public List<Predicate<T>> getFilters() {
        return filters;
    }

    public List<Comparator<T>> getSorts() {
        return sorts;
    }

    public Optional<String> getQuery() {
        return query;
    }
}
