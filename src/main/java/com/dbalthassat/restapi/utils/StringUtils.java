package com.dbalthassat.restapi.utils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

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

    public static URL createURLExcludingGetParams(HttpServletRequest request, Collection<String> exclude) {
        String urlString = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        URL url = URL.buildUrl(urlString, queryString);
        exclude.forEach(url::removeParam);
        return url;
    }
}
