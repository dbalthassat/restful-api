package com.dbalthassat.restapi.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;

public class PrettyMappingJackson2HttpMessageConverter extends DefaultMappingJackson2HttpMessageConverter {

    /**
    * Construct a new {@link MappingJackson2HttpMessageConverter} using default configuration
    * provided by {@link Jackson2ObjectMapperBuilder}
    */
    public PrettyMappingJackson2HttpMessageConverter() {
        super();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        setSupportedMediaTypes(Collections.singletonList(new MediaType("application", "json+pretty", DEFAULT_CHARSET)));
    }
}