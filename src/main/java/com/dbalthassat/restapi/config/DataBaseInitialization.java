package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.entity.Messages;
import com.dbalthassat.restapi.property.EnvProperty;
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
    private EnvProperty envProperty;

    @PostConstruct
    public void init() {
        if(Application.DEV_ENVIRONMENT.equals(envProperty.getProfile())) {
            initGreetings();
        }
    }

    public void initGreetings() {
        List<Greetings> greetings = new LinkedList<>();
        Greetings withMessage = new Greetings("world");
        withMessage.addMessage(new Messages("HIGH", "high message"));
        withMessage.addMessage(new Messages("LOW", "low message"));
        greetings.add(withMessage);
        Greetings withMessage2 = new Greetings("tata");
        withMessage2.addMessage(new Messages("MEDIUM", "medium message"));
        greetings.add(withMessage2);
        greetings.add(new Greetings("toto", "A small description"));
        greetings.add(new Greetings("a", "B"));
        greetings.add(new Greetings("titi"));
        greetingsRepository.save(greetings);
    }
}
