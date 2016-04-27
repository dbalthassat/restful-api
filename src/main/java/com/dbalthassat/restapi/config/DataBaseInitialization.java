package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.entity.GreetingMessages;
import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.property.EnvProperty;
import com.dbalthassat.restapi.repository.GreetingMessagesRepository;
import com.dbalthassat.restapi.repository.GreetingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class DataBaseInitialization {
    @Autowired
    private GreetingsRepository greetingsRepository;

    @Autowired
    private GreetingMessagesRepository greetingMessagesRepository;

    @Autowired
    private EnvProperty envProperty;

    @PostConstruct
    public void init() {
        if(Application.DEV_ENVIRONMENT.equals(envProperty.getProfile())) {
            initGreetings();
        }
    }

    public void initGreetings() {
        List<Greetings> greetings = new LinkedList<>();
        greetings.add(new Greetings("world"));
        greetings.add(new Greetings("tata"));
        greetings.add(new Greetings("toto", "A small description"));
        greetings.add(new Greetings("a", "B"));
        greetings.add(new Greetings("titi"));
        greetingsRepository.save(greetings);
        Greetings greetingsWithMessage1 = greetingsRepository.findOne(1L);
        Greetings greetingsWithMessage2 = greetingsRepository.findOne(2L);
        List<GreetingMessages> greetingMessages = new LinkedList<>();
        greetingMessages.add(new GreetingMessages(greetingsWithMessage1, new Messages("HIGH", "high message")));
        greetingMessages.add(new GreetingMessages(greetingsWithMessage1, new Messages("LOW", "low message")));
        greetingMessages.add(new GreetingMessages(greetingsWithMessage2, new Messages("MEDIUM", "medium message")));
        greetingMessagesRepository.save(greetingMessages);
    }
}
