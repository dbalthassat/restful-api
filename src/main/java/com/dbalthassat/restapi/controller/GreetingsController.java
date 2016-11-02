package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.dao.GreetingsDao;
import com.dbalthassat.restapi.service.GreetingsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingsController extends GenericController<GreetingsDao, GreetingsService> {
    @Override
    protected Class<GreetingsDao> daoClass() {
        return GreetingsDao.class;
    }
}