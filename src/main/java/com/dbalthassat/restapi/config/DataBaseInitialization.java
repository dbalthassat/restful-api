package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.property.EnvProperty;
import com.dbalthassat.restapi.repository.GreetingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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
       /* List<Greetings> greetings = new LinkedList<>();
        greetings.add(new Greetings("world"));
        greetings.add(new Greetings("tata"));
        greetings.add(new Greetings("toto", "A small description"));
        greetings.add(new Greetings("a", "B"));
        greetings.add(new Greetings("titi"));
        greetingsRepository.save(greetings);*/
    }
}
