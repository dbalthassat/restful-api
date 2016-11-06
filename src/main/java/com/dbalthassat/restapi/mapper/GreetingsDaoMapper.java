package com.dbalthassat.restapi.mapper;

import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.entity.MessagesEntity;

import java.util.Collections;
import java.util.List;

public class GreetingsDaoMapper implements GenericMapper {
    @Override
    public List<Converter<? extends ApiEntity, ?>> converters() {
        return Collections.singletonList(new Converter<>(MessagesEntity.class, String.class, MessagesEntity::getValue));
    }
}
