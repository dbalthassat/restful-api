package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.config.EntityMappers;
import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.utils.GenericUtils;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

// TODO refactoring parce que c'est dégueulasse
class MapperUtils {
	private enum Direction {
		TO_ENTITY, TO_DAO;
	}
	private final static Logger LOGGER = getLogger(MapperUtils.class);

	private MapperUtils() {}

	public static <DAO extends ApiDao> DAO mapEmptyFields(ApiEntity entity, Class<DAO> daoClass, List<Converter<? extends ApiEntity, ?>> converters) {
		try {
			DAO dao = daoClass.getConstructor().newInstance();
			mapEmptyFields(entity, daoClass, converters, Direction.TO_DAO);
			return dao;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace(); // TODO
			return null;
		}
	}

	public static <ENTITY extends ApiEntity> ENTITY mapEmptyFields(ApiDao dao, Class<ENTITY> entityClass, List<Converter<? extends ApiEntity, ?>> converters) {
		try {
			ENTITY entity = entityClass.getConstructor().newInstance();
			mapEmptyFields(entity, dao, converters, Direction.TO_ENTITY);
			return entity;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace(); // TODO
			return null;
		}
	}

	private static Object mapEmptyFields(ApiEntity entity, Object dao, List<Converter<? extends ApiEntity, ?>> converters, Direction direction) {
		try {
			if(direction == Direction.TO_DAO) {
				handleToDao(entity, dao, converters, direction);
			} else {
				handleToEntity(entity, dao, converters, direction);
			}
		} catch (IllegalAccessException e) {
			// TODO handle exception
		}
		return dao;
	}

	private static void handleToEntity(ApiEntity entity, Object dao, List<Converter<? extends ApiEntity, ?>> converters, Direction direction) throws IllegalAccessException {
		List<Field> daoFields = GenericUtils.findFields(dao.getClass());
		List<Field> entityFields = GenericUtils.findFields(entity.getClass());
		for(Field entityField: entityFields) {
			Optional<Field> opDaoField = daoFields.stream().filter(f -> f.getName().equals(entityField.getName())).findFirst();
			if(!opDaoField.isPresent()) {
				LOGGER.debug("Field " + entityField.getName() + " of class " + entity.getClass().getName() + " does not exist in " + dao.getClass().getName());
			} else {
				Field daoField = opDaoField.get();
				mapField(entity, converters, dao, daoField, entityField, direction);
			}
		}
	}

	private static void handleToDao(ApiEntity entity, Object dao, List<Converter<? extends ApiEntity, ?>> converters, Direction direction) throws IllegalAccessException {
		List<Field> daoFields = GenericUtils.findFields(dao.getClass());
		List<Field> entityFields = GenericUtils.findFields(entity.getClass());
		for(Field daoField: daoFields) {
			Optional<Field> opEntityField = entityFields.stream().filter(f -> f.getName().equals(daoField.getName())).findFirst();
			if(!opEntityField.isPresent()) {
				LOGGER.debug("Field " + daoField.getName() + " of class " + dao.getClass().getName() + " does not exist in " + entity.getClass().getName());
			} else {
				Field entityField = opEntityField.get();
				mapField(entity, converters, dao, daoField, entityField, direction);
			}
		}
	}

	private static void mapField(ApiEntity entity, List<Converter<? extends ApiEntity, ?>> converters, Object dao, Field daoField, Field entityField, Direction direction) throws IllegalAccessException {
		if(!(handleLists(entity, dao, daoField, entityField, converters, direction) ||
				handleConverters(entity, dao, entityField, daoField, converters, direction) ||
				handleEntityMappers(entity, dao, entityField, daoField, converters, direction) ||
				handleSameTypes(entity, dao, entityField, daoField, direction))) {
			// TODO log en fonction de la direction
			LOGGER.debug("Field " + daoField.getName() + " of class " + daoField.getDeclaringClass().getName() + " has not been mapped because types does not match.");
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean handleLists(ApiEntity entity, Object dao, Field daoField, Field entityField, List<Converter<? extends ApiEntity, ?>> converters, Direction direction) throws IllegalAccessException {
		if(entityField.getType().equals(List.class) && daoField.getType().equals(List.class)) {

			/*ParameterizedType entityType = (ParameterizedType) entityField.getGenericType();
			ParameterizedType daoType = (ParameterizedType) daoField.getGenericType();
			Optional<Converter<? extends ApiEntity, ?>> op = converters.stream()
					.filter(converter -> converter.getInClass().equals(entityType.getActualTypeArguments()[0]) && converter.getOutClass().equals(daoType.getActualTypeArguments()[0]))
					.findAny();
			if(op.isPresent()) {
				// TODO vieux hack pour éviter les problèmes de compil... (suppression des wildcards)
				Converter converter = op.get();
				try {
					if(direction == Direction.TO_DAO && ((List) daoField.get(dao)).isEmpty()) {
						List<?> entityList = (List<?>) entityField.get(entity);
						daoField.set(dao, entityList.stream()
								.map(converter::convertRight)
								.collect(Collectors.toList()));
					} else if(((List) entityField.get(entity)).isEmpty()) {
						List<?> daoList = (List<?>) daoField.get(dao);
						entityField.set(entity, daoList.stream()
								.map(converter::convertLeft)
								.collect(Collectors.toList()));
					}
					return true;
				} catch(Exception e) {
					System.out.println(e);
					// TODO log
				}
			}*/
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static boolean handleConverters(ApiEntity entity, Object dao, Field entityField, Field daoField, List<Converter<? extends ApiEntity, ?>> converters, Direction direction) {
		Class<?> entityType = entityField.getType();
		Class<?> daoType = daoField.getType();
		Optional<Converter<? extends ApiEntity, ?>> op = converters.stream()
				.filter(converter -> converter.getInClass().equals(entityType) && converter.getOutClass().equals(daoType))
				.findAny();
		if(op.isPresent()) {
			// TODO vieux hack pour éviter les problèmes de compil... (suppression des wildcards)
			Converter converter = op.get();
			try {
				if(direction == Direction.TO_ENTITY && entityField.get(entity) == null) {
					entityField.set(entity, converter.convertRight(dao));
				} else if(daoField.get(dao) == null) {
					daoField.set(dao, converter.convertLeft(entity));
				}
				return true;
			} catch (IllegalAccessException e) {
				e.printStackTrace(); // TODO
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static boolean handleEntityMappers(ApiEntity entity, Object dao, Field entityField, Field daoField, List<Converter<? extends ApiEntity, ?>> converters, Direction direction) throws IllegalAccessException {
		if(daoField.get(dao) == null && daoField.getType().isAssignableFrom(ApiDao.class)) {
			Optional<EntityMapper> opMapper = EntityMappers.findWithClasses(entity.getClass(), (Class<? extends ApiDao>) dao.getClass());
			if(opMapper.isPresent()) {
				EntityMapper mapper = opMapper.get();
				if(direction == Direction.TO_DAO) {
					daoField.set(dao, mapper.map(entity));
				} else {
					entityField.set(entity, mapper.map((ApiDao) dao));
				}
				return true;
			}
		}
		return false;
	}

	private static boolean handleSameTypes(ApiEntity from, Object to, Field fromField, Field toField, Direction direction) throws IllegalAccessException {
		if(direction == Direction.TO_DAO) {
			return handleSameTypesToDao(from, to, fromField, toField);
		}
		return handleSameTypesToEntity(from, to, fromField, toField);
	}

	private static boolean handleSameTypesToEntity(ApiEntity entity, Object dao, Field entityField, Field daoField) throws IllegalAccessException {
		if(daoField.getType().equals(entityField.getType()) && entityField.get(entity) == null) {
			entityField.set(entity, daoField.get(dao));
			return true;
		}
		return false;
	}

	private static boolean handleSameTypesToDao(ApiEntity entity, Object dao, Field entityField, Field daoField) throws IllegalAccessException {
		if(!(daoField.getGenericType() instanceof ParameterizedType) && daoField.getType().equals(entityField.getType()) && daoField.get(dao) == null) {
			daoField.set(dao, entityField.get(entity));
			return true;
		}
		return false;
	}
}
