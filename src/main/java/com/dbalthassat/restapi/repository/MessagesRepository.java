package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.entity.QMessages;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

public interface MessagesRepository extends GenericRepository<Messages> {
    default Page<Messages> findByGreetingId(HttpServletRequest request, Long greetingsId) {
        BooleanExpression condition = QMessages.messages.greetings().id.eq(greetingsId);//QMessages.messages.greetings().greetings().id.eq(greetingsId);
        return findAll(request, condition, "messages");
    }
}
