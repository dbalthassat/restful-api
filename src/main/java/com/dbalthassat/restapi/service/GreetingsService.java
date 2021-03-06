package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.exception.clientError.notFound.IdNotFoundException;
import com.dbalthassat.restapi.repository.GreetingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class GreetingsService {
    private final GreetingsRepository repository;

    @Autowired
    public GreetingsService(GreetingsRepository repository) {
        this.repository = repository;
    }

    public Greetings findOne(Long id) {
        Optional<Greetings> op = Optional.ofNullable(repository.findOne(id));
        return op.orElseThrow(() -> new IdNotFoundException(id));
    }

    public Greetings createGreeting(Greetings greetings) {
        return repository.save(greetings);
    }

    public Iterable<Greetings> findAll(HttpServletRequest request) {
        return repository.findAll(request);
    }

    public List<Greetings> save(List<Greetings> greetings) {
        return repository.save(greetings);
    }

    public Greetings delete(Long id) {
        Greetings result = repository.findOne(id);
        repository.delete(result);
        if(result == null) {
            throw new IdNotFoundException(id);
        }
        return result;
    }
}
