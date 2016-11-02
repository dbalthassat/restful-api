package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.entity.GreetingsEntity;
import com.dbalthassat.restapi.entity.MessagesEntity;
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
        List<GreetingsEntity> greetings = new LinkedList<>();
        GreetingsEntity withMessage = new GreetingsEntity("world");
        withMessage.addMessage(new MessagesEntity("HIGH", "high message"));
        withMessage.addMessage(new MessagesEntity("LOW", "low message"));
        greetings.add(withMessage);
        GreetingsEntity withMessage2 = new GreetingsEntity("tata");
        withMessage2.addMessage(new MessagesEntity("MEDIUM", "medium message"));
        greetings.add(withMessage2);
        greetings.add(new GreetingsEntity("toto", "A small description"));
        greetings.add(new GreetingsEntity("a", "B"));
        greetings.add(new GreetingsEntity("titi"));
        greetingsRepository.save(greetings);
    }
}
