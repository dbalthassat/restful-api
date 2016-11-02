package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.dao.GenericDao;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.repository.GenericRepository;
import com.dbalthassat.restapi.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public abstract class GenericController<DAO extends GenericDao, SERVICE extends GenericService<DAO, ?, ? extends GenericRepository<?>, ? extends GenericMapper<DAO, ?>>> {
    @Autowired
    private SERVICE service;

    @RequestMapping({"", "/"})
    public List<?> findAll() {
        return service.findAll(daoClass());
    }

    protected abstract Class<DAO> daoClass();
}
