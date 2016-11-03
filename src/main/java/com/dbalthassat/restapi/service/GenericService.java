package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.dao.GenericDao;
import com.dbalthassat.restapi.entity.GenericEntity;
import com.dbalthassat.restapi.exception.clientError.notFound.EmptyResourceException;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.stream.Collectors;

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

    public DAO findFirst(Class<DAO> clazz) {
        long count = repository.count();
        if(count == 0) {
            throw new EmptyResourceException();
        }
        Pageable req = buildPageable(0, 1);
        Page<ENTITY> res = repository.findAll(req);
        return res.map(e -> map(clazz, e)).getContent().get(0);
    }

    public DAO findLast(Class<DAO> clazz) {
        long count = repository.count();
        if(count == 0) {
            throw new EmptyResourceException();
        }
        Pageable req = buildPageable((int) count - 1, 1);
        Page<ENTITY> res = repository.findAll(req);
        return res.map(e -> map(clazz, e)).getContent().get(0);
    }

    public Page<DAO> findAll(Class<DAO> clazz, int page, int size) {
        Pageable req = buildPageable(page, size);
        Page<ENTITY> res = repository.findAll(req);
        return res.map(e -> map(clazz, e));
    }

    public static Pageable buildPageable(int page, int size) {
        return new PageRequest(page, size);
    }

    public DAO map(Class<DAO> clazz, ENTITY entity) {
        return mapper.map(entity, clazz);
    }

    public Collection<DAO> map(Class<DAO> clazz, Collection<ENTITY> entities) {
        return entities.stream().map(e -> map(clazz, e)).collect(Collectors.toList());
    }

    public Page<DAO> map(Class<DAO> clazz, Page<ENTITY> entities) {
        return entities.map(e -> map(clazz, e));
    }
}
