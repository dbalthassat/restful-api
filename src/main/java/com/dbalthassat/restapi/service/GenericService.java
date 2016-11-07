package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.config.EntityMappers;
import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.exception.clientError.notFound.ResourceNotFoundException;
import com.dbalthassat.restapi.repository.GenericRepository;
import com.dbalthassat.restapi.utils.EntityMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GenericService {
    private static final Logger LOGGER = getLogger(GenericService.class);
    private static final String LOG_ENTITY_NOT_FOUND = "No entity mapper found for resource {}. Did you forgot to declare it in class EntityMappers?";
    @Autowired
    private GenericRepository repository;

    @Autowired
    private EntityMappers mappers;

    public List<? extends ApiDao> findAll(String resource) {
        EntityMapper entityMapper = findEntityMapper(resource);
        List<? extends ApiEntity> entities = repository.findAll(entityMapper);
        return entityMapper.map(entities);
    }

    public ApiDao findFirst(String resource) {
        EntityMapper entityMapper = findEntityMapper(resource);
        ApiEntity entity = repository.findFirst(entityMapper);
        return entityMapper.map(entity);
    }

    public ApiDao findOne(String resource, Long id) {
        EntityMapper entityMapper = findEntityMapper(resource);
        ApiEntity entity = repository.findOne(entityMapper, id);
        return entityMapper.map(entity);
    }

    public List<? extends ApiDao> findAll(String parent, Long parentId, String resource) {
        EntityMapper entityMapperParent = findEntityMapper(parent);
        EntityMapper entityMapper = findEntityMapper(resource);
        List<? extends ApiEntity> entities = repository.findAllWithParent(entityMapperParent, parentId, entityMapper);
        return entityMapper.map(entities);
    }


    public ApiDao findOneWithParent(String parent, Long parentId, String resource, Long id) {
        EntityMapper entityMapperParent = findEntityMapper(parent);
        EntityMapper entityMapper = findEntityMapper(resource);
        ApiEntity entities = repository.findOneWithParent(entityMapperParent, parentId, entityMapper, id);
        return entityMapper.map(entities);
    }

    private EntityMapper findEntityMapper(String entityName) {
        return mappers.findMapperWithName(entityName).orElseThrow(() -> {
            LOGGER.debug(LOG_ENTITY_NOT_FOUND, entityName);
            return new ResourceNotFoundException(entityName);
        });
    }
}

