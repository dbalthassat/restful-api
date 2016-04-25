package com.dbalthassat.restapi.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class LocalDateTimeISO8601Deserializer extends JsonDeserializer<LocalDateTime>  {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return ZonedDateTime.parse(jsonParser.getValueAsString()).toLocalDateTime();
    }
}
