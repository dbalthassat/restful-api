package com.dbalthassat.restapi.utils;

import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.exception.IllegalParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParamsUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ParamsUtils.class);
    private final static String[] RESERVED_WORDS = { "sort", "fields", "q" };

    private ParamsUtils() {}

    public static <T extends ApiEntity, E extends T> List<T> apply(Map<String, String[]> parameterMap, List<E> values) {
        Params<T> params  = parseParams(parameterMap, values.get(0).getClass());
        Stream<E> stream = values.stream();
        for (Predicate<T> filter : params.getFilters()) {
            stream = stream.filter(filter);
        }
        if(params.getQuery().isPresent()) {
            stream = stream.filter(e -> queryFilter(params.getQuery().get(), e));
        }
        Collections.reverse(params.getSorts());
        for(Comparator<T> sort: params.getSorts()) {
            stream = stream.sorted(sort);
        }
        params.getSorts().forEach(e -> {});
        return stream.collect(Collectors.toList());
    }

    private static <T extends ApiEntity, E extends T> boolean queryFilter(String query, E value) {
        return value.query(query);
    }

    private static <T extends ApiEntity> Params<T> parseParams(Map<String, String[]> parameterMap, Class<? extends ApiEntity> clazz) {
        String[] sort = parameterMap.get("sort");
        String[] q = parameterMap.get("q");
        removeReservedWords(parameterMap);
        List<Predicate<T>> filters = handleFilters(parameterMap, clazz);
        Optional<String> query = handleQuery(q);
        List<Comparator<T>> sorts = handleSort(sort, clazz);
        return new Params<>(filters, sorts, query);
    }

    private static void removeReservedWords(Map<String, String[]> parameterMap) {
        for(String word: RESERVED_WORDS) {
            parameterMap.remove(word);
        }
    }

    private static Optional<String> handleQuery(String[] q) {
        try {
            return ArrayUtils.findFirstElement(q);
        } catch(IllegalArgumentException e) {
            throw new IllegalParameterException("The parameter q accepts only one value.");
        }
    }

    private static <T extends ApiEntity> List<Comparator<T>> handleSort(String[] sortsRequest, Class<? extends ApiEntity> clazz) {
        if(sortsRequest == null || sortsRequest.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(sortsRequest[0].split(",")).collect(Collectors.toList())
                .stream()
                .map(String::trim)
                .map(ParamsUtils::addDefaultSortDirectionIfNeeded)
                .map(sort -> ParamsUtils.<T>buildSort(sort, clazz))
                .collect(Collectors.toList());
    }

    private static String addDefaultSortDirectionIfNeeded(String sort) {
        if(!sort.startsWith("+") && !sort.startsWith("-")) {
            return "+" + sort;
        }
        return sort;
    }

    @SuppressWarnings("unchecked")
    private static <T extends ApiEntity> Comparator<T> buildSort(String sort, Class<? extends ApiEntity> clazz) {
        int direction = sort.startsWith("-") ? -1 : 1;
        sort = sort.substring(1);
        Method getter = buildGetter(clazz, sort);
        return (a, b) -> {
            try {
                Comparable<Object> comparableA = (Comparable<Object>) getter.invoke(a);
                Comparable<Object> comparableB = (Comparable<Object>) getter.invoke(b);
                if(comparableA == null && comparableB == null) {
                    return 0;
                } else if(comparableA == null) {
                    return 1;
                } else if(comparableB == null) {
                    return -1;
                }
                return comparableA.compareTo(comparableB) * direction;
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Should not happen.
                LOGGER.error(e.getMessage(), e);
            }
            return 0;
        };
    }

    // TODO gérer les objets imbriqués
    private static <T extends ApiEntity> List<Predicate<T>> handleFilters(Map<String, String[]> parameterMap, Class<? extends ApiEntity> clazz) {
        return parameterMap.entrySet().stream()
                .map(parameterEntry -> ParamsUtils.<T>buildFilter(clazz, parameterEntry)).collect(Collectors.toList());
    }

    private static <T extends ApiEntity> Predicate<T> buildFilter(Class<? extends ApiEntity> clazz, Map.Entry<String, String[]> parameterEntry) {
        String fieldName = parameterEntry.getKey();
        String[] params = parameterEntry.getValue();
        Predicate<T> condition = t -> false;
        for(String param: params) {
            condition = buildCondition(fieldName, clazz, condition, param);
        }
        return condition;
    }

    private static <T extends ApiEntity> Predicate<T> buildCondition(String fieldName, Class<? extends ApiEntity> clazz,
                                                                     Predicate<T> condition, String param) {
        return condition.or(t -> {
            try {
                Optional<Boolean> value = findConditionDependingOnType(fieldName, clazz, param, t);
                return value.orElse(false);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Should not happen.
                LOGGER.error(e.getMessage(), e);
            }
            return true;
        });
    }

    private static <T extends ApiEntity> Optional<Boolean> findConditionDependingOnType(String fieldName, Class<? extends ApiEntity> clazz,
                                                                                        String param, T t) throws IllegalAccessException, InvocationTargetException {
        Method getter = buildGetter(clazz, fieldName);
        Object value = getter.invoke(t);
        Optional<Boolean> result = Optional.empty();
        if(value instanceof String) {
            result = Optional.of(param.equals(value));
        } else if(value instanceof Enum<?>) {
            result = Optional.of(param.equals(((Enum<?>) value).name()));
        } else if(value instanceof Long) {
            try {
                result = Optional.of(Long.parseLong(param) == (Long) value);
            } catch(NumberFormatException e) {
                throw new IllegalParameterException("The value of field '%s' should be a number.", fieldName);
            }

        }
        return result;
    }

    private static Method buildGetter(Class<? extends ApiEntity> clazz, String fieldName) {
        return Arrays.stream(clazz.getDeclaredMethods())
                    .filter(isGetter(fieldName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalParameterException("The field '%s' does not exist.", fieldName));
    }

    private static Predicate<Method> isGetter(String fieldName) {
        return e -> StringUtils.isGetter(fieldName, e);
    }
}
