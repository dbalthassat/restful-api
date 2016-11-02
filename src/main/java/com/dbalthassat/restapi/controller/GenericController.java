package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.GenericEntity;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.repository.GenericRepository;
import com.dbalthassat.restapi.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public abstract class GenericController<ENTITY extends GenericEntity, SERVICE extends GenericService<ENTITY, ? extends GenericRepository<ENTITY>, ? extends GenericMapper<ENTITY>>> {
    @Autowired
    private SERVICE service;

    @RequestMapping({"", "/"})
    public List<?> findAll() {
        return service.findAll(daoClass());
    }

    protected abstract Class<?> daoClass();
}
