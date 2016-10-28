package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.repository.MessagesRepository;
import com.dbalthassat.restapi.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/greetings/{greetingsId}/messages")
public class GreetingsMessagesController extends GenericController<Messages, MessagesRepository> {
    private final MessagesService service;

    @Autowired
    public GreetingsMessagesController(MessagesService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public Iterable<Messages> getMessages(HttpServletRequest request, @PathVariable(value = "greetingsId") Long greetingsId) {
        return service.findByGreetingsId(request, greetingsId);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Iterable<Messages> getMessage(HttpServletRequest request, @PathVariable(value = "greetingsId") Long greetingsId, @PathVariable(value = "id") Long id) {
        return service.findByIdAndGreetingsId(request, greetingsId, id);
    }
}
