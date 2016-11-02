package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.dao.GenericDao;
import com.dbalthassat.restapi.entity.GenericEntity;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class GenericService<
            DAO extends GenericDao,
            ENTITY extends GenericEntity,
            REPOSITORY extends GenericRepository<ENTITY>,
            MAPPER extends GenericMapper<DAO, ENTITY>
        > {
    @Autowired
    private REPOSITORY repository;

    @Autowired
    private MAPPER mapper;

    public List<DAO> findAll(Class<DAO> clazz) {
        List<ENTITY> list = repository.findAll();
        return mapper.map(list, clazz);
    }
}
