package com.dbalthassat.quizrc.utils;

import com.dbalthassat.quizrc.entity.Entity;
import com.dbalthassat.quizrc.exception.IllegalParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParamsUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ParamsUtils.class);

    private ParamsUtils() {}

    public static <T extends Entity, E extends T> List<T> apply(Map<String, String[]> parameterMap, List<E> values) {
        Params<T> params  = parseParams(parameterMap, values.get(0).getClass());
        Stream<E> stream = values.stream();
        for (Predicate<T> filter : params.getFilters()) {
            stream = stream.filter(filter);
        }
        if(params.getQuery().isPresent()) {
            stream = stream.filter(e -> e.query(params.getQuery().get()));
        }
        return stream.collect(Collectors.toList());
    }

    private static <T extends Entity> Params<T> parseParams(Map<String, String[]> parameterMap, Class<? extends Entity> clazz) {
        List<Predicate<T>> filters = handleFilters(parameterMap, clazz);
        handleSort(parameterMap.get("sort"));
        Optional<String> query = handleQuery(parameterMap.get("q"));
        return new Params<>(filters, query);
    }

    private static Optional<String> handleQuery(String[] q) {
        try {
            return ArrayUtils.findFirstElement(q);
        } catch(IllegalArgumentException e) {
            throw new IllegalParameterException("The parameter q accepts only one value.");
        }
    }

    // TODO gérer les tris
    private static void handleSort(String[] sorts) {

    }

    // TODO gérer les objets imbriqués
    // TODO refactorer ce code dégueulasse
    private static <T extends Entity> List<Predicate<T>> handleFilters(Map<String, String[]> parameterMap, Class<? extends Entity> clazz) {
        String[] prefixes = {"get", "is"}; // TODO paramétrer cela

        return parameterMap.entrySet().stream()
                .filter(e -> !"sort".equals(e.getKey()) && !"q".equals(e.getKey())) // TODO faire ce filtre en amont
                .map(parameterEntry -> {
                    String fieldName = parameterEntry.getKey();
                    String[] params = parameterEntry.getValue();
                    Method method = Arrays.stream(clazz.getDeclaredMethods())
                            .filter(e -> e.getName().equals(fieldName)) // TODO prendre en compte les préfixes
                            .findFirst()
                            .orElseThrow(() -> new IllegalParameterException("The filter {} cannot be apply.")); // TODO ajouter le paramètre
                    Predicate<T> condition = t -> false;
                    for(String param: params) {
                        condition = condition.or(t -> {
                            try {
                                Object value = method.invoke(t);
                                if(value instanceof String) {
                                    String v = (String) value;
                                    return param.equals(v);
                                } else if(value instanceof Enum<?>) {
                                    Enum<?> v = (Enum<?>) value;
                                    return param.equals(v.name());
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                // Should not happen.
                                LOGGER.error(e.getMessage(), e);
                            }
                            // If this error occurs, we do not apply this filter.
                            return true;
                        });
                    }
                    return condition;
                }).collect(Collectors.toList());
    }
}
