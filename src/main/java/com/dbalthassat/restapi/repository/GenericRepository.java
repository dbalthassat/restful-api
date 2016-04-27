package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.QGreetingMessages;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.servlet.http.HttpServletRequest;

@NoRepositoryBean
public interface GenericRepository<T> extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {
    default Page<T> findAll(HttpServletRequest request) {
        Predicate predicate = (Predicate) request.getAttribute("predicate");
        Pageable pageable = (Pageable) request.getAttribute("pageable");
        return findAll(predicate, pageable);
    }

    default Page<T> findAll(HttpServletRequest request, String fieldName, Long id) {
        Predicate predicate = (Predicate) request.getAttribute("predicate");
        BooleanExpression expression = QGreetingMessages.greetingMessages.greetings().id.longValue().eq(1L);
        Pageable pageable = (Pageable) request.getAttribute("pageable");
        return findAll(expression, pageable);
    }
}
