package com.dbalthassat.restapi.entity;

import com.dbalthassat.restapi.exception.NotQueryableException;

/**
 * This interface should be implemented by objects which are queryable. This means the API will accept
 * the parameter {@code q} to process a research on the object.
 */
public interface Queryable {
    /**
     * Each object which implements this interface can, or cannot, be requested with a parameter {@code q}.
     * If this method is implemented, the API will accept this parameter and reseach the value
     * specified in the request looking the result of this method. The matching between the user
     * value and the result of this method is done by {@link Queryable#query(String)}.
     *
     * @see Queryable#query(String)
     * @throws NotQueryableException if the method has not been implemented.
     * @return a value which will match, or not match, with the query of the user.
     */
    default String queryValue() {
        throw new NotQueryableException("This request does not accept parameter q.");
    }

    /**
     * If the entity is queryable, this method will compare the result of the method {@link Queryable#queryValue()}
     * and the {@code value} specified by the user. The result is true if the API should accept the {@code value},
     * false otherwise.
     *
     * @param value the value specified by the user in the request.
     *
     * @see Queryable#queryValue()
     * @return true if {@code value} should be accept, false otherwise.
     */
    default boolean query(String value) {
        return queryValue().contains(value);
    }
}
