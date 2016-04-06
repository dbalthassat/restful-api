package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.entity.ApiEntity;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class HandleFieldsRestriction implements ResponseBodyAdvice<Iterable<? extends ApiEntity>> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return !"com.dbalthassat.restapi.controller.ExceptionController".equals(methodParameter.getMethod().getDeclaringClass().getName());
    }

    @Override
    public Iterable<? extends ApiEntity> beforeBodyWrite(Iterable<? extends ApiEntity> apiEntities, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String param = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getParameter("fields");
        if(param == null) {
            return apiEntities;
        }
        List<String> requestedFields = Arrays.stream(param.split(",")).collect(Collectors.toList());
        for(ApiEntity entity: apiEntities) {
            try {
                Field[] fields = entity.getClass().getDeclaredFields();
                for(Field field: fields) {
                    if(!requestedFields.contains(field.getName())) {
                        field.setAccessible(true);
                        field.set(entity, null);
                        field.setAccessible(false);
                    }
                }
            } catch (IllegalAccessException e) {
                // Should not happen.
            }
        }
        return apiEntities;
    }
}
