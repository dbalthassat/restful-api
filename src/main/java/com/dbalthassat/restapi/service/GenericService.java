package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.entity.GenericEntity;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class GenericService<
        ENTITY extends GenericEntity,
        REPOSITORY extends GenericRepository<ENTITY>,
        MAPPER extends GenericMapper<ENTITY>> {
    @Autowired
    private REPOSITORY repository;

    @Autowired
    private MAPPER mapper;

    public List<?> findAll(Class<?> clazz) {
        List<ENTITY> list = repository.findAll();
        return mapper.map(list, clazz);
    }
}
