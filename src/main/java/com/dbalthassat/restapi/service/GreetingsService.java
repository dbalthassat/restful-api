package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.entity.GreetingsEntity;
import com.dbalthassat.restapi.mapper.GreetingsMapper;
import com.dbalthassat.restapi.repository.GreetingsRepository;
import org.springframework.stereotype.Service;

@Service
public class GreetingsService extends GenericService<GreetingsEntity, GreetingsRepository, GreetingsMapper> {
}
