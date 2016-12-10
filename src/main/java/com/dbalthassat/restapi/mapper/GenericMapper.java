package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.entity.ApiEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface GenericMapper {
    default <DAO extends ApiDao> DAO mapEntity(ApiEntity entity, Class<DAO> daoClass) {
        return MapperUtils.mapEmptyFields(entity, daoClass, converters());
    }

    default <DAO extends ApiDao> List<DAO> mapEntities(List<? extends ApiEntity> entities, Class<DAO> daoClass) {
        return entities.stream().map(e -> mapEntity(e, daoClass)).collect(Collectors.toList());
    }

    default <ENTITY extends ApiEntity> ENTITY mapDao(ApiDao dao, Class<ENTITY> entityClass) {
        return MapperUtils.mapEmptyFields(dao, entityClass, converters());
    }

    default <ENTITY extends ApiEntity> List<ENTITY> mapDaos(List<? extends ApiDao> daos, Class<ENTITY> entityClass) {
        return daos.stream().map(e -> mapDao(e, entityClass)).collect(Collectors.toList());
    }

    default List<Converter<? extends ApiEntity, ?>> converters() {
        return Collections.emptyList();
	}
}
