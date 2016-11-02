package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.dao.GreetingsDao;
import com.dbalthassat.restapi.entity.GreetingsEntity;
import com.dbalthassat.restapi.service.GreetingsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingsController extends GenericController<GreetingsEntity, GreetingsService> {
    @Override
    protected Class<?> daoClass() {
        return GreetingsDao.class;
    }
}