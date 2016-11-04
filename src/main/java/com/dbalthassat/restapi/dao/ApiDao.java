package com.dbalthassat.restapi.dao;

import com.dbalthassat.restapi.config.LocalDateTimeISO8601Deserializer;
import com.dbalthassat.restapi.config.LocalDateTimeISO8601Serializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

public class ApiDao {
    private Long id;

    @JsonSerialize(using = LocalDateTimeISO8601Serializer.class)
    @JsonDeserialize(using = LocalDateTimeISO8601Deserializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeISO8601Serializer.class)
    @JsonDeserialize(using = LocalDateTimeISO8601Deserializer.class)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
