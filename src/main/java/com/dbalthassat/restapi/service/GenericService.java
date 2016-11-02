package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.dao.GenericDao;
import com.dbalthassat.restapi.entity.GenericEntity;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    public Page<DAO> findAll(Class<DAO> clazz, int page, int size) {
        PageRequest req = new PageRequest(page, size);
        Page<ENTITY> res = repository.findAll(req);
        return res.map(e -> mapper.map(e, clazz));
    }
}
