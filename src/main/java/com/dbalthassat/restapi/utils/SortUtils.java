package com.dbalthassat.restapi.utils;

import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SortUtils {
    private SortUtils() {}

    public static String addSortDirectionIfNeeded(String s) {
        if (!s.startsWith("+") && !s.startsWith("-")) {
            return "+" + s;
        }
        return s;
    }

    public static List<Sort> createSortFromStringList(Optional<String> sortStringOp) {
        if(!sortStringOp.isPresent()) {
            return Collections.emptyList();
        }
        String sortString = sortStringOp.get();
        return Arrays.stream(sortString.split(",")).collect(Collectors.toList())
                .stream()
                .map(String::trim)
                .map(SortUtils::addSortDirectionIfNeeded)
                .map(SortUtils::createSortFromString)
                .collect(Collectors.toList());
    }

    public static Sort createSortFromString(String s) {
        return new Sort("-".equals(s.substring(0, 1)) ? Sort.Direction.DESC : Sort.Direction.ASC, s.substring(1));
    }

    public static Sort createSortFromList(List<Sort> sorts) {
        Sort sort = null;
        for(Sort s: sorts) {
            if(sort == null) {
                sort = s;
            } else {
                sort = sort.and(s);
            }
        }
        return sort;
    }
}
