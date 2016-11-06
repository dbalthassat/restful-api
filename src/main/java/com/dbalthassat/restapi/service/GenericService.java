package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.config.EntityMappers;
import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.exception.clientError.notFound.ResourceNotFoundException;
import com.dbalthassat.restapi.repository.GenericRepository;
import com.dbalthassat.restapi.utils.EntityMapper;
import com.dbalthassat.restapi.utils.HttpUtils;
import com.dbalthassat.restapi.utils.Resource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GenericService {
    private static final Logger LOGGER = getLogger(GenericService.class);
    private static final String LOG_ENTITY_NOT_FOUND = "No entity mapper found for resource {}. Did you forgot to declare it in class EntityMappers?";
    @Autowired
    private GenericRepository repository;

    @Autowired
    private EntityMappers mappers;

    public List<? extends ApiDao> findAll(String uri) {
        List<Resource> resources = HttpUtils.findResources(uri);
        String entityName = resources.get(0).getResource();
        Optional<EntityMapper> op = mappers.findMapperWithName(entityName);
        if(op.isPresent()) {
            EntityMapper entityMapper = op.get();
            List<? extends ApiEntity> entities = repository.findAll(entityMapper);
            return entityMapper.map(entities);
        }
        LOGGER.debug(LOG_ENTITY_NOT_FOUND, entityName);
        throw new ResourceNotFoundException(HttpMethod.GET.name(), uri);
    }

    public ApiDao findOne(String uri) {
        List<Resource> resources = HttpUtils.findResources(uri);
        String entityName = resources.get(0).getResource();
        Long id = resources.get(0).getId();
        Optional<EntityMapper> op = mappers.findMapperWithName(entityName);
        if(op.isPresent()) {
            EntityMapper entityMapper = op.get();
            ApiEntity entity = repository.findOne(entityMapper, id);
            return entityMapper.map(entity);
        }
        LOGGER.debug(LOG_ENTITY_NOT_FOUND, entityName);
        throw new ResourceNotFoundException(HttpMethod.GET.name(), uri);
    }

    public List<? extends ApiDao> findAllWithParent(String uri) {
        List<Resource> resources = HttpUtils.findResources(uri);
        String parent = resources.get(0).getResource();
        Long parentId = resources.get(0).getId();
        String entityName = resources.get(1).getResource();
        Optional<EntityMapper> opParent = mappers.findMapperWithName(parent);
        Optional<EntityMapper> op = mappers.findMapperWithName(entityName);
        if(opParent.isPresent() && op.isPresent()) {
            EntityMapper entityMapperParent = opParent.get();
            EntityMapper entityMapper = op.get();
            List<? extends ApiEntity> entities = repository.findAllWithParent(entityMapperParent, parentId, entityMapper);
            return entityMapper.map(entities);
        }
        if(!opParent.isPresent()) {
            LOGGER.debug(LOG_ENTITY_NOT_FOUND, parent);
        }
        if(!op.isPresent()) {
            LOGGER.debug(LOG_ENTITY_NOT_FOUND, entityName);
        }
        throw new ResourceNotFoundException(HttpMethod.GET.name(), uri);
    }

    public ApiDao findOneWithParent(String uri) {
        List<Resource> resources = HttpUtils.findResources(uri);
        String parent = resources.get(0).getResource();
        Long parentId = resources.get(0).getId();
        String entityName = resources.get(1).getResource();
        Long id = resources.get(1).getId();
        Optional<EntityMapper> opParent = mappers.findMapperWithName(parent);
        Optional<EntityMapper> op = mappers.findMapperWithName(entityName);
        if(opParent.isPresent() && op.isPresent()) {
            EntityMapper entityMapperParent = opParent.get();
            EntityMapper entityMapper = op.get();
            ApiEntity entities = repository.findOneWithParent(entityMapperParent, parentId, entityMapper, id);
            return entityMapper.map(entities);
        }
        if(!opParent.isPresent()) {
            LOGGER.debug(LOG_ENTITY_NOT_FOUND, parent);
        }
        if(!op.isPresent()) {
            LOGGER.debug(LOG_ENTITY_NOT_FOUND, entityName);
        }
        throw new ResourceNotFoundException(HttpMethod.GET.name(), uri);
    }
}

