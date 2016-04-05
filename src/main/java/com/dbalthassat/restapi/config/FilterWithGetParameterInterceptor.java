package com.dbalthassat.restapi.config;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class FilterWithGetParameterInterceptor extends HandlerInterceptorAdapter {
    private final static String BASE_PACKAGE = Application.BASE_PACKAGE + ".entity";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.remove("sort");
        params.remove("q");
        String resourceName = request.getServletPath().substring(1);
        String className = BASE_PACKAGE + ".Q" + resourceName.substring(0, 1).toUpperCase() + resourceName.substring(1);
        Object resource = Class.forName(className).getDeclaredField("greetings").get(null);
        BooleanExpression predicate = null;
        for(Map.Entry<String, String[]> param: params.entrySet()) {
            Object result = Class.forName(className).getDeclaredField(param.getKey()).get(resource);
            if(result instanceof StringPath) {
                if(predicate == null) {
                    predicate = ((StringPath) result).eq(param.getValue()[0]);
                } else {
                    predicate.and(((StringPath) result).eq(param.getValue()[0]));
                }

            }
        }
        request.setAttribute("predicate", predicate);
        return super.preHandle(request, response, handler);
    }
}
