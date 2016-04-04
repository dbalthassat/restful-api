package com.dbalthassat.restapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class WebConfiguration {
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new DefaultMappingJackson2HttpMessageConverter();
    }

    @Bean
    public MappingJackson2HttpMessageConverter prettyMappingJackson2HttpMessageConverter() {
        return new PrettyMappingJackson2HttpMessageConverter();
    }
}
