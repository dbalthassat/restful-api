package com.dbalthassat.quizrc.utils;

import java.util.List;
import java.util.function.Predicate;

public class Params<T> {
    private final List<Predicate<T>> filters;

    public Params(List<Predicate<T>> filters) {
        this.filters = filters;
    }

    public List<Predicate<T>> getFilters() {
        return filters;
    }
}
