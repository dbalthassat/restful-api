package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public Page<Messages> findByGreetingsId(HttpServletRequest request, Long greetingsId) {
        return messagesRepository.findByGreetingsId(request, greetingsId);
    }

    public Iterable<Messages> findByIdAndGreetingsId(HttpServletRequest request, Long greetingsId, Long id) {
        return messagesRepository.findByGreetingsIdAndId(request, greetingsId, id);
    }
}
