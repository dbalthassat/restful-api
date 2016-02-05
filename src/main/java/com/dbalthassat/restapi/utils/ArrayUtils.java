package com.dbalthassat.restapi.utils;

import java.util.Optional;

public class ArrayUtils {
    private ArrayUtils() {}

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> Optional<T> findFirstElement(T[] array) {
        Optional<T> result;
        if(isEmpty(array)) {
            result = Optional.empty();
        } else if(array.length > 1) {
            throw new IllegalArgumentException();
        } else {
            result = Optional.of(array[0]);
        }
        return result;
    }
}
