package com.dbalthassat.restapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private FilterWithGetParameterInterceptor filterWithGetParameterInterceptor;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new DefaultMappingJackson2HttpMessageConverter();
    }

    @Bean
    public MappingJackson2HttpMessageConverter prettyMappingJackson2HttpMessageConverter() {
        return new PrettyMappingJackson2HttpMessageConverter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(filterWithGetParameterInterceptor);
    }
}
