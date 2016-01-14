package com.dbalthassat.quizrc.utils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public class ParamsUtils {
    public static <T> Params<T> parseParams(Map<String, String[]> parameterMap) {
        String[] sort = parameterMap.get("sort");
        String[] q = parameterMap.get("q");
        parameterMap.entrySet().stream()
                .filter(e -> !"sort".equals(e.getKey()) && !"q".equals(e.getKey()))
                .forEach(e -> {});
        // TODO transformer parameterMap en filters
        Params<T> params = null;// = new Params<T>(filters);
        return params;
    }

    public static <T> Collection<T> apply(Collection<T> values, Params<T> params) {
        Stream<T> stream = values.stream();
        params.getFilters().forEach(stream::filter);
        return null;
        // TODO implémenter q ... Mais voir comment faire car cela dépend vraiment de l'entité
    }
}
