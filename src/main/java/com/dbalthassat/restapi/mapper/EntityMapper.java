package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.entity.ApiEntity;

import java.util.List;

public class EntityMapper<ENTITY extends ApiEntity, DAO extends ApiDao> {
    private final String resource;
    private final Class<ENTITY> entityClass;
    private final Class<DAO> daoClass;
    private final GenericMapper mapper;

    public EntityMapper(String resource, Class<ENTITY> entityClass, Class<DAO> daoClass, GenericMapper mapper) {
        this.resource = resource;
        this.entityClass = entityClass;
        this.daoClass = daoClass;
        this.mapper = mapper;
    }

    public String getResource() {
        return resource;
    }

    public Class<ENTITY> getEntityClass() {
        return entityClass;
    }

    public Class<DAO> getDaoClass() {
        return daoClass;
    }

    public DAO map(ApiEntity entity) {
        return mapper.mapEntity(entity, daoClass);
    }

    @SuppressWarnings("unchecked")
    public List<DAO> map(List<ENTITY> entities) {
        return mapper.mapEntities(entities, daoClass);
    }

    public ENTITY map(ApiDao dao) {
        return mapper.mapDao(dao, entityClass);
    }
}
