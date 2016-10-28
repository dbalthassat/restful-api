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

    public static String capitalizeFirstLetter(String str) {
        if(str == null || str.isEmpty()) {
            throw new IllegalArgumentException("The string cannot be empty.");
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isGetter(String fieldName, Method method) {
        boolean isGetter = false;
        for (String prefix : GETTER_PREFIXES) {
            isGetter |= method.getName().equals(StringUtils.camelCase(prefix, fieldName));
        }
        return isGetter;
    }

    public static String splitAndGetElementAtIndexDesc(String str, String regex, int idx) {
        String[] split = str.split(regex);
        if(idx >= split.length) {
            throw new IllegalArgumentException("The string `" + str + "` does not contain anything at index " + idx + " (split with regex: `" + regex + "`.)");
        }
        return split[split.length - 1 - idx];
    }
}
