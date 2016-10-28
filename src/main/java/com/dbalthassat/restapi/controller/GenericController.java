package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.exception.clientError.notFound.DataEmptyThenElementNotFoundException;
import com.dbalthassat.restapi.repository.GenericRepository;
import com.dbalthassat.restapi.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public abstract class GenericController<ENTITY extends ApiEntity, REPOSITORY extends GenericRepository<ENTITY>> {
    @Autowired
    private REPOSITORY repository;

    @RequestMapping("/count")
    public long count() {
        return repository.count();
    }

    @RequestMapping("/first")
    public ENTITY first(HttpServletRequest request) {
        Optional<ENTITY> element = repository.findFirst();
        return element.orElseThrow(() ->
            new DataEmptyThenElementNotFoundException(request.getServletPath().substring(0, request.getServletPath().lastIndexOf('/')))
        );
    }

    @RequestMapping("/last")
    public ENTITY last(HttpServletRequest request) {
        Optional<ENTITY> element = repository.findLast();
        return element.orElseThrow(() ->
            new DataEmptyThenElementNotFoundException(StringUtils.splitAndGetElementAtIndexDesc(request.getServletPath(), "/", 1))
        );
    }
}
