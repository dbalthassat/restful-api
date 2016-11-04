package com.dbalthassat.restapi.mapper;

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

public interface GenericMapper {
    Logger LOGGER = getLogger(GenericMapper.class);

    default <DAO> DAO mapEmptyFields(Object entity, Class<DAO> clazz) {
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

    default <DAO> boolean handleConverters(Object entity, DAO dao, Field daoField, Field entityField) {
        Class<?> entityType = entityField.getType();
        Class<?> daoType = daoField.getType();
        Optional<Converter<?, ?>> op = converters().stream()
                .filter(c -> c.getInClass().equals(entityType) &&
                        c.getOutClass().equals(daoType))
                .findAny();
        if(op.isPresent()) {
            Converter converter = op.get();
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
    default <DAO> boolean handleLists(Object entity, DAO dao, Field daoField, Field entityField) throws IllegalAccessException {
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

    default <DAO> boolean handleSameTypes(Object entity, DAO dao, Field daoField, Field entityField) throws IllegalAccessException {
        if(daoField.getType().equals(entityField.getType())) {
            daoField.set(dao, entityField.get(entity));
            return true;
        }
        return false;
    }

    default <DAO> List<DAO> mapEmptyFields(List<Object> entities, Class<DAO> clazz) {
        return entities.stream().map(e -> map(e, clazz)).collect(Collectors.toList());
    }

    default <DAO> DAO map(Object entity, Class<DAO> clazz) {
        return mapEmptyFields(entity, clazz);
    }

    default <DAO> List<DAO> map(List<Object> entities, Class<DAO> clazz) {
        return mapEmptyFields(entities, clazz);
    }

    default List<Converter<?, ?>> converters() {
        return Collections.emptyList();
    }
}
