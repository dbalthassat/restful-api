package com.dbalthassat.restapi.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class DefaultMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    /**
     * Construct a new {@link MappingJackson2HttpMessageConverter} using default configuration
     * provided by {@link Jackson2ObjectMapperBuilder}
     */
    public DefaultMappingJackson2HttpMessageConverter() {
        super();
        objectMapper.registerModule(new Hibernate4Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}