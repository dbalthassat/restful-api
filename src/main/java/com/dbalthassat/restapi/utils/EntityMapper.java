package com.dbalthassat.restapi.utils;

import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.mapper.GenericMapper;

import java.util.List;

public class EntityMapper {
    private final String resource;
    private final Class<? extends ApiEntity> entityClass;
    private final ApiDao dao;
    private final GenericMapper mapper;

    public EntityMapper(String resource, Class<? extends ApiEntity> entityClass, ApiDao dao, GenericMapper mapper) {
        this.resource = resource;
        this.entityClass = entityClass;
        this.dao = dao;
        this.mapper = mapper;
    }

    public String getResource() {
        return resource;
    }

    public Class<? extends ApiEntity> getEntityClass() {
        return entityClass;
    }

    public <E extends ApiEntity> ApiDao map(E entity) {
        return mapper.mapEntity(entity, dao.getClass());
    }

    @SuppressWarnings("unchecked")
    public List<? extends ApiDao> map(List<? extends ApiEntity> entities) {
                return mapper.mapEntities(entities, dao.getClass());
    }
}
