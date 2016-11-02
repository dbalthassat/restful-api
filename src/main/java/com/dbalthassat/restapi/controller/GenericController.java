package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.dao.GenericDao;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.property.RestProperty;
import com.dbalthassat.restapi.repository.GenericRepository;
import com.dbalthassat.restapi.service.GenericService;
import com.dbalthassat.restapi.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class GenericController<DAO extends GenericDao, SERVICE extends GenericService<DAO, ?, ? extends GenericRepository<?>, ? extends GenericMapper<DAO, ?>>> {
    @Autowired
    private SERVICE service;

    @Autowired
    private RestProperty restProperty;

    @RequestMapping({"", "/"})
    public List<?> findAll(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "size", required = false) Integer size) {
        page = page == null ? restProperty.getDefaultPage() : page;
        size = size == null ? restProperty.getDefaultSize() : size;
        Page<?> result = service.findAll(daoClass(), page - 1, size);
        List<?> content = result.getContent();
        HttpUtils.buildCountHeader(response, content);
        HttpUtils.buildLinkHeader(request, response, page, size, result);
        return content;
    }

    protected abstract Class<DAO> daoClass();
}
