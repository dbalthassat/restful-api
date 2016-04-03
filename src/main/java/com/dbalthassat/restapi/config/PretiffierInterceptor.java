package com.dbalthassat.restapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PretiffierInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PretiffierInterceptor.class);

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String pretiffy = request.getParameter("pretiffy");
        Boolean isPretiffy = pretiffy != null;
        LOGGER.debug("Response should {}be pretiffied.", isPretiffy ? "" : "not ");
        mappingJackson2HttpMessageConverter.setPrettyPrint(isPretiffy);
        return true;
    }
}