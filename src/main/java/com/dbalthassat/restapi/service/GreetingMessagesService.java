package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.repository.GreetingMessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class GreetingMessagesService {
    private final GreetingMessagesRepository repository;

    @Autowired
    public GreetingMessagesService(GreetingMessagesRepository repository) {
        this.repository = repository;
    }

    public Page<Messages> findByGreetingsId(HttpServletRequest request, Long greetingsId) {
        return repository.findByGreetingId(request, greetingsId);
    }
}
