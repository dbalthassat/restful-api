package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.entity.GenericEntity;
import com.dbalthassat.restapi.utils.GenericUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GenericMapper<ENTITY extends GenericEntity> {
    default <DAO> DAO map(ENTITY entity, Class<DAO> clazz) {
        DAO dao = null;
        try {
            dao = clazz.getConstructor().newInstance();
            List<Field> daoFields = GenericUtils.findFields(clazz);
            List<Field> entityFields = GenericUtils.findFields(entity.getClass());
            for(Field daoField: daoFields) {
                Optional<Field> entityField = entityFields.stream().filter(f -> f.getName().equals(daoField.getName())).findFirst();
                if(!entityField.isPresent()) {
                    // TODO maybe log ?
                } else {
                    daoField.set(dao, entityField.get().get(entity));
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO gestion
        }
        return dao;
    }

    default <DAO> List<DAO> map(List<ENTITY> entities, Class<DAO> clazz) {
        return entities.stream().map(e -> map(e, clazz)).collect(Collectors.toList());
    }
}
