package com.dbalthassat.quizrc.entity;

import com.dbalthassat.quizrc.exception.NotQueryableException;

public interface Entity {
    default String queryValue() {
        throw new NotQueryableException("This request does not accept parameter q.");
    }
}
