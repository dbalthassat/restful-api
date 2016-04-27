package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.GreetingMessages;
import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.entity.QGreetingMessages;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

public interface GreetingMessagesRepository extends GenericRepository<GreetingMessages> {
    default Page<Messages> findByGreetingId(HttpServletRequest request, Long greetingsId) {
        BooleanExpression condition = QGreetingMessages.greetingMessages.greetings().id.eq(greetingsId);
        return findAll(request, "messages", condition).map(GreetingMessages::getMessages);
    }
}
