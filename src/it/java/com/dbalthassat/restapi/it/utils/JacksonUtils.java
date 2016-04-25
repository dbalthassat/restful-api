package com.dbalthassat.restapi.it.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

public class JacksonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JacksonUtils() {}

    public static <T> T getResponse(MvcResult result, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), clazz);
    }
}
