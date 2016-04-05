package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.Greetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface GreetingRepository extends JpaRepository<Greetings, Long>, QueryDslPredicateExecutor<Greetings> {
}
