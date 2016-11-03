package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.dao.GenericDao;
import com.dbalthassat.restapi.entity.GenericEntity;
import com.dbalthassat.restapi.utils.GenericUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GenericMapper<DAO extends GenericDao, ENTITY extends GenericEntity> {
    default DAO mapEmptyFields(ENTITY entity, Class<DAO> clazz) {
        DAO dao = null;
        try {
            dao = clazz.getConstructor().newInstance();
            List<Field> daoFields = GenericUtils.findFields(clazz);
            List<Field> entityFields = GenericUtils.findFields(entity.getClass());
            for(Field daoField: daoFields) {
                Optional<Field> entityField = entityFields.stream().filter(f -> f.getName().equals(daoField.getName())).findFirst();
                if(!entityField.isPresent()) {
                    // TODO maybe log ?
                    System.out.println("toto");
                } else if(daoField.get(dao) == null) { // Mapping only if the entityField is null
                    if(!(handleLists(entity, dao, daoField, entityField.get()) ||
                            handleSameTypes(entity, dao, daoField, entityField.get()))) {
                        System.out.println("paf");
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO handle exception
        }
        return dao;
    }

    @SuppressWarnings("unchecked")
    default boolean handleLists(ENTITY entity, DAO dao, Field daoField, Field entityField) throws IllegalAccessException {
        if(entityField.getType().equals(List.class) && daoField.getType().equals(List.class)) {
            ParameterizedType entityType = (ParameterizedType) entityField.getGenericType();
            ParameterizedType daoType = (ParameterizedType) daoField.getGenericType();
            List<?> entityList = (List<?>) entityField.get(entity);
            Optional<Converter<?, ?>> op = converters().stream()
                    .filter(c -> c.getInClass().equals(entityType.getActualTypeArguments()[0]) &&
                                 c.getOutClass().equals(daoType.getActualTypeArguments()[0]))
                    .findAny();
            Converter converter = op.get();
            try {
                daoField.set(dao, entityList.stream()
                        .map(converter::convert)
                        .collect(Collectors.toList()));
                return true;
            } catch(Exception e) {
                System.out.println(e);
                // TODO log
            }
        }
        return false;
    }

    default boolean handleSameTypes(ENTITY entity, DAO dao, Field daoField, Field entityField) throws IllegalAccessException {
        if(daoField.getType().equals(entityField.getType())) {
            daoField.set(dao, entityField.get(entity));
            return true;
        }
        return false;
    }

    default List<DAO> mapEmptyFields(List<ENTITY> entities, Class<DAO> clazz) {
        return entities.stream().map(e -> map(e, clazz)).collect(Collectors.toList());
    }

    default DAO map(ENTITY entity, Class<DAO> clazz) {
        return mapEmptyFields(entity, clazz);
    }

    default List<DAO> map(List<ENTITY> entities, Class<DAO> clazz) {
        return mapEmptyFields(entities, clazz);
    }

    List<Converter<?, ?>> converters();
}
