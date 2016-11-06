package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.utils.GenericUtils;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

// TODO refactoring parce que c'est dégueulasse
public interface GenericMapper {
    Logger LOGGER = getLogger(GenericMapper.class);

    default <ENTITY extends ApiEntity, DAO> DAO mapEmptyFields(ENTITY entity, Class<DAO> clazz) {
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
                            handleConverters(entity, dao, daoField, entityField.get()) ||
                            handleSameTypes(entity, dao, daoField, entityField.get()))) {
                        LOGGER.debug("Field " + daoField.getName()
                                     + " of class " + daoField.getDeclaringClass().getName()
                                     + " has not been mapped because types does not match.");
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO handle exception
        }
        return dao;
    }

    @SuppressWarnings("unchecked")
    default <ENTITY extends ApiEntity, DAO> boolean handleConverters(ENTITY entity, DAO dao, Field daoField, Field entityField) {
        Class<?> entityType = entityField.getType();
        Class<?> daoType = daoField.getType();
        Optional<Converter<? extends ApiEntity, ?>> op = converters().stream()
                .filter(c -> c.getInClass().equals(entityType) &&
                        c.getOutClass().equals(daoType))
                .findAny();
        if(op.isPresent()) {
            Converter<ENTITY, ?> converter = (Converter<ENTITY, ?>) op.get();
            try {
                daoField.set(dao, converter.convert(entity));
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    default <ENTITY extends ApiEntity, DAO> boolean handleLists(ENTITY entity, DAO dao, Field daoField, Field entityField) throws IllegalAccessException {
        if(entityField.getType().equals(List.class) && daoField.getType().equals(List.class)) {
            ParameterizedType entityType = (ParameterizedType) entityField.getGenericType();
            ParameterizedType daoType = (ParameterizedType) daoField.getGenericType();
            List<ENTITY> entityList = (List<ENTITY>) entityField.get(entity);
            Optional<Converter<? extends ApiEntity, ?>> op = converters().stream()
                    .filter(c -> c.getInClass().equals(entityType.getActualTypeArguments()[0]) &&
                                 c.getOutClass().equals(daoType.getActualTypeArguments()[0]))
                    .findAny();
            if(op.isPresent()) {
                Converter<ENTITY, ?> converter = (Converter<ENTITY, ?>) op.get();
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
        }
        return false;
    }

    default <ENTITY extends ApiEntity, DAO> boolean handleSameTypes(ENTITY entity, DAO dao, Field daoField, Field entityField) throws IllegalAccessException {
        if(daoField.getType().equals(entityField.getType())) {
            daoField.set(dao, entityField.get(entity));
            return true;
        }
        return false;
    }

    default <ENTITY extends ApiEntity, DAO> List<DAO> mapEmptyFields(List<ENTITY> entities, Class<DAO> clazz) {
        return entities.stream().map(e -> mapEntity(e, clazz)).collect(Collectors.toList());
    }

    default <ENTITY extends ApiEntity, DAO> DAO mapEntity(ENTITY entity, Class<DAO> clazz) {
        return mapEmptyFields(entity, clazz);
    }

    default <ENTITY extends ApiEntity, DAO> List<DAO> mapEntities(List<ENTITY> entities, Class<DAO> clazz) {
        return mapEmptyFields(entities, clazz);
    }

    default List<Converter<? extends ApiEntity, ?>> converters() {
        return Collections.emptyList();
    }
}
