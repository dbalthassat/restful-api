package com.dbalthassat.restapi.utils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class StringUtils {
    private final static String[] GETTER_PREFIXES = {"get", "is"};

    public static String camelCase(String first, String... next) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(next)
                .forEach(e -> builder.append(capitalizeFirstLetter(e)));
        return first + builder.toString();
    }

    public static String capitalizeFirstLetter(String e) {
        if(e == null || e.isEmpty()) {
            throw new IllegalArgumentException("The string cannot be empty.");
        }
        return e.substring(0, 1).toUpperCase() + e.substring(1);
    }

    public static boolean isGetter(String fieldName, Method e) {
        boolean isGetter = false;
        for (String prefix : GETTER_PREFIXES) {
            isGetter |= e.getName().equals(StringUtils.camelCase(prefix, fieldName));
        }
        return isGetter;
    }
}
