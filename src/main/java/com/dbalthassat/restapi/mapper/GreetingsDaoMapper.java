package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.dao.GreetingsDao;
import com.dbalthassat.restapi.entity.GreetingsEntity;
import com.dbalthassat.restapi.entity.MessagesEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GreetingsDaoMapper implements GenericMapper<GreetingsDao, GreetingsEntity> {
    @Override
    public List<Converter<?, ?>> converters() {
        return Arrays.asList(
                new Converter<>(MessagesEntity.class, Long.class, MessagesEntity::getId),
                new Converter<>(MessagesEntity.class, String.class, MessagesEntity::getValue)
        );
    }
}
