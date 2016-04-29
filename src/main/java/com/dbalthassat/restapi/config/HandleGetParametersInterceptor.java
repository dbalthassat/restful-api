package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.exception.clientError.badRequest.FieldDoesNotExistException;
import com.dbalthassat.restapi.utils.ArrayUtils;
import com.dbalthassat.restapi.utils.SortUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// TODO gérer paramètre q
@Component
public class HandleGetParametersInterceptor extends HandlerInterceptorAdapter {
    private final static String[] RESERVED_PARAMS = { "pageNumber", "pageSize", "sort", "fields" };
    private final static String BASE_PACKAGE = Application.BASE_PACKAGE + ".entity";

    @Override
    // TODO créer une annotation et exécuter ce code uniquement si le handler a cette annotation
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        Optional<String> pageNumber = removeParam(params, "pageNumber");
        Optional<String> pageSize = removeParam(params, "pageSize");
        Optional<String> sort = removeParam(params, "sort");
        handleFilters(request, params);
        handlePagination(request, pageNumber, pageSize, sort);
        return super.preHandle(request, response, handler);
    }

    private static Optional<String> removeParam(Map<String, String[]> params, String pageNumber) {
        String[] param = params.remove(pageNumber);
        return param == null || param.length == 0 ? Optional.empty() : Optional.of(param[0]);
    }

    private void handlePagination(HttpServletRequest request, Optional<String> pageNumberOp, Optional<String> pageSizeOp, Optional<String> sortStringOp) {
        // TODO mettre les params en property
        int pageNumber = Integer.parseInt(pageNumberOp.orElse("1"));
        int pageSize = Integer.parseInt(pageSizeOp.orElse("10"));
        List<Sort> sorts = SortUtils.createSortFromStringList(sortStringOp);
        Sort sort = SortUtils.createSortFromList(sorts);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        request.setAttribute("pageable", pageRequest);
    }

    private void handleFilters(HttpServletRequest request, Map<String, String[]> params) {
        String resourceName = request.getServletPath().substring(request.getServletPath().lastIndexOf("/") + 1);
        if(resourceName.isEmpty()) {
            // Nothing to do, we handle resource calls only.
            return;
        }
        String className = BASE_PACKAGE + ".Q" + resourceName.substring(0, 1).toUpperCase() + resourceName.substring(1);
        try {
            Object resource = Class.forName(className).getDeclaredField(resourceName).get(null);
            BooleanBuilder predicate = createFilter(params, className, resource);
            request.setAttribute("predicate", predicate);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            // Nothing to do, if we catch an error here, the resource does not exist and the server will send a 404.
        }
    }

    private BooleanBuilder createFilter(Map<String, String[]> params, String className, Object resource) {
        List<BooleanExpression> predicates = new LinkedList<>();
        for(Map.Entry<String, String[]> param: params.entrySet()) {
            String fieldName = param.getKey();
            if(!ArrayUtils.contains(RESERVED_PARAMS, fieldName)) {
                Object field = findField(className, resource, fieldName);
                String fieldValue = param.getValue()[0];
                predicates.add(createCondition(field, fieldValue));
            }
        }
        BooleanBuilder builder = new BooleanBuilder();
        predicates.forEach(builder::and);
        return builder;
    }

    private Object findField(String className, Object resource, String fieldName) {
        Object field;
        try {
            field = Class.forName(className).getDeclaredField(fieldName).get(resource);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            throw new FieldDoesNotExistException(fieldName);
        }
        return field;
    }

    @SuppressWarnings("unchecked")
    private BooleanExpression createCondition(Object field, String fieldValue) {
        BooleanExpression exp = null;
        if(field instanceof StringPath) {
            exp = ((StringPath) field).eq(fieldValue);
        } else if(field instanceof NumberPath) {
            exp = ((NumberPath) field).longValue().eq(Long.parseLong(fieldValue));
        }
        return exp;
    }
}
