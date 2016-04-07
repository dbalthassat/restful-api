package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.entity.ApiEntity;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class HandleFieldsRestrictionOnEntity implements ResponseBodyAdvice<ApiEntity> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return ApiEntity.class.equals(methodParameter.getMethod().getReturnType().getSuperclass());
    }

    @Override
    public ApiEntity beforeBodyWrite(ApiEntity apiEntity, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String param = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getParameter("fields");
        if(param == null) {
            return apiEntity;
        }
        List<String> requestedFields = Arrays.stream(param.split(",")).collect(Collectors.toList());
        ReflectionUtils.doWithFields(apiEntity.getClass(), field -> {
            if(!requestedFields.contains(field.getName())) {
                field.set(apiEntity, null);
            }
        });
        return apiEntity;
    }
}
