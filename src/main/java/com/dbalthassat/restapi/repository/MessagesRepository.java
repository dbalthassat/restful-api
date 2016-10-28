package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.entity.QMessages;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

public interface MessagesRepository extends GenericRepository<Messages> {
    default Page<Messages> findByGreetingsId(HttpServletRequest request, Long greetingsId) {
        BooleanExpression condition = QMessages.messages.greetings().id.eq(greetingsId);
        return findAll(request, condition, "messages");
    }

    default Iterable<Messages> findByGreetingsIdAndId(HttpServletRequest request, Long greetingsId, Long id) {
        BooleanExpression condition = QMessages.messages.greetings().id.eq(greetingsId);
        condition = condition.and(QMessages.messages.id.eq(id));
        return findAll(request, condition, "messages");
    }

}
