package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.dao.GreetingsDao;
import com.dbalthassat.restapi.dao.MessagesDao;
import com.dbalthassat.restapi.entity.GreetingsEntity;
import com.dbalthassat.restapi.entity.MessagesEntity;
import com.dbalthassat.restapi.mapper.GreetingsDaoMapper;
import com.dbalthassat.restapi.mapper.MessagesDaoMapper;
import com.dbalthassat.restapi.utils.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class EntityMappers {
    private final static List<EntityMapper> MAPPERS;

    static {
        MAPPERS = new LinkedList<>();
        MAPPERS.add(new EntityMapper("greetings", GreetingsEntity.class, GreetingsDao.class, new GreetingsDaoMapper()));
        MAPPERS.add(new EntityMapper("messages", MessagesEntity.class, MessagesDao.class, new MessagesDaoMapper()));
    }

    public Optional<EntityMapper> findMapperWithName(String resourceName) {
        return MAPPERS.stream()
                      .filter(m -> m.getResource().equals(resourceName))
                      .findAny();
    }
}
