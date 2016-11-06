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
    private final static List<EntityMapper> mappers;

    static {
        mappers = new LinkedList<>();
        mappers.add(new EntityMapper("greetings", GreetingsEntity.class, new GreetingsDao(), new GreetingsDaoMapper()));
        mappers.add(new EntityMapper("messages", MessagesEntity.class, new MessagesDao(), new MessagesDaoMapper()));
    }

    public Optional<EntityMapper> findMapperWithName(String resourceName) {
        return mappers.stream()
                      .filter(m -> m.getResource().equals(resourceName))
                      .findAny();
    }
}
