package com.dbalthassat.restapi.entity;

import com.dbalthassat.restapi.exception.NotQueryableException;

public interface ApiEntity extends Queryable {
    /**
     * {@inheritDoc}
     *
     * @throws NotQueryableException if the method has not been implemented.
     */
    @Override
    default String queryValue() {
        throw new NotQueryableException("This request does not accept parameter q.");
    }
}
