package com.dbalthassat.restapi.repository;

import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.servlet.http.HttpServletRequest;

@NoRepositoryBean
public interface GenericRepository<T> extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {
    default Iterable<T> findAll(HttpServletRequest request) {
        Predicate predicate = (Predicate) request.getAttribute("predicate");
        Pageable pageable = (Pageable) request.getAttribute("pageable");
        return findAll(predicate, pageable);
    }
}
