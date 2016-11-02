package com.dbalthassat.restapi.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class GenericUtils {
    private GenericUtils() {}

    public static List<Field> findFields(Class<?> clazz) {
        List<Field> fields = new LinkedList<>();
        ReflectionUtils.doWithFields(clazz, field -> {
            field.setAccessible(true);
            fields.add(field);
        });
        return fields;
    }
}
