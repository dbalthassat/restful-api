package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.dao.GreetingsDao;
import com.dbalthassat.restapi.dao.MessagesDao;
import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.entity.GreetingsEntity;
import com.dbalthassat.restapi.entity.MessagesEntity;
import com.dbalthassat.restapi.mapper.EntityMapper;
import com.dbalthassat.restapi.mapper.GreetingsDaoMapper;
import com.dbalthassat.restapi.mapper.MessagesDaoMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EntityMappers {
    private final static List<EntityMapper> MAPPERS;

    static {
        MAPPERS = new LinkedList<>();
        MAPPERS.add(new EntityMapper("greetings", GreetingsEntity.class, GreetingsDao.class, new GreetingsDaoMapper()));
        MAPPERS.add(new EntityMapper("messages", MessagesEntity.class, MessagesDao.class, new MessagesDaoMapper()));
    }

    public static Optional<EntityMapper> findMapperWithName(String resourceName) {
        return MAPPERS.stream()
                      .filter(m -> m.getResource().equals(resourceName))
                      .findAny();
    }

    public static Optional<EntityMapper> findWithClasses(Class<? extends ApiEntity> entityClass, Class<? extends ApiDao> daoClass) {
        return MAPPERS.stream()
                      .filter(m -> m.getDaoClass().equals(daoClass) && m.getEntityClass().equals(entityClass))
                      .findAny();
    }
}
