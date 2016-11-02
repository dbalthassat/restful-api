package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.dao.GreetingsDao;
import com.dbalthassat.restapi.entity.GreetingsEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GreetingsDaoMapper implements GenericMapper<GreetingsDao, GreetingsEntity> {
    @Override
    public GreetingsDao map(GreetingsEntity entity, Class<GreetingsDao> clazz) {
        GreetingsDao dao = mapEmptyFields(entity, clazz);
        dao.setCreatedAt(LocalDateTime.now());
        return dao;
    }
}
