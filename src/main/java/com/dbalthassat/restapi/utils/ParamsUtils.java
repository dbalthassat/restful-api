package com.dbalthassat.restapi.utils;

import com.dbalthassat.restapi.entity.Entity;
import com.dbalthassat.restapi.exception.IllegalParameterException;
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
            stream = stream.filter(e -> queryFilter(params.getQuery().get(), e));
        }
        return stream.collect(Collectors.toList());
    }

    private static <T extends Entity, E extends T> boolean queryFilter(String query, E value) {
        return value.query(query);
    }

    private static <T extends Entity> Params<T> parseParams(Map<String, String[]> parameterMap, Class<? extends Entity> clazz) {
        String[] sort = parameterMap.remove("sort");
        String[] q = parameterMap.remove("q");
        List<Predicate<T>> filters = handleFilters(parameterMap, clazz);
        Optional<String> query = handleQuery(q);
        handleSort(sort);
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
    private static <T extends Entity> List<Predicate<T>> handleFilters(Map<String, String[]> parameterMap, Class<? extends Entity> clazz) {
        return parameterMap.entrySet().stream()
                .map(parameterEntry -> ParamsUtils.<T>buildFilter(clazz, parameterEntry)).collect(Collectors.toList());
    }

    private static <T extends Entity> Predicate<T> buildFilter(Class<? extends Entity> clazz, Map.Entry<String, String[]> parameterEntry) {
        String fieldName = parameterEntry.getKey();
        String[] params = parameterEntry.getValue();
        Method getter = buildGetter(clazz, fieldName);
        Predicate<T> condition = t -> false;
        for(String param: params) {
            condition = buildCondition(getter, condition, param);
        }
        return condition;
    }

    private static <T extends Entity> Predicate<T> buildCondition(Method getter, Predicate<T> condition, String param) {
        return condition.or(t -> {
            try {
                Optional<Boolean> value = findConditionDependingOnType(getter, param, t);
                if(value.isPresent()) {
                    return value.get();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Should not happen.
                LOGGER.error(e.getMessage(), e);
            }
            // If this error occurs, we do not apply this filter.
            return true;
        });
    }

    private static <T extends Entity> Optional<Boolean> findConditionDependingOnType(Method getter, String param, T t) throws IllegalAccessException, InvocationTargetException {
        Object value = getter.invoke(t);
        Optional<Boolean> result = Optional.empty();
        if(value instanceof String) {
            result = Optional.of(param.equals(value));
        } else if(value instanceof Enum<?>) {
            result = Optional.of(param.equals(((Enum<?>) value).name()));
        }
        return result;
    }

    private static Method buildGetter(Class<? extends Entity> clazz, String fieldName) {
        return Arrays.stream(clazz.getDeclaredMethods())
                    .filter(isGetter(fieldName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalParameterException("The filter %s cannot be apply.", fieldName));
    }

    private static Predicate<Method> isGetter(String fieldName) {
        return e -> StringUtils.isGetter(fieldName, e);
    }
}
