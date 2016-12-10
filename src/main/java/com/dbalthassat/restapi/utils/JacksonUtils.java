package com.dbalthassat.restapi.utils;

import com.dbalthassat.restapi.exception.serverError.JsonFormatException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JacksonUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private JacksonUtils() {}

	public static <T> T convert(String json, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			throw new JsonFormatException(e);
		}
	}
}
