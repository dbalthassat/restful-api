package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.exception.clientError.NotFoundException;
import com.dbalthassat.restapi.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/greetings")
// TODO versioning, cf http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
// TODO add an alias
public class GreetingsController {
    private static final String TEMPLATE = "Hello, %s!";
    private static final String DEFAULT_NAME = "world";

    @Autowired
    private GreetingRepository repository;

    @PostConstruct
    public void initBdd() {
        List<Greetings> greetings = new LinkedList<>();
        greetings.add(new Greetings("world"));
        greetings.add(new Greetings("tata"));
        greetings.add(new Greetings("toto", "A small description"));
        greetings.add(new Greetings("a", "B"));
        greetings.add(new Greetings("titi"));
        repository.save(greetings);
    }

    @ResponseBody
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public Iterable<Greetings> get(HttpServletRequest request) throws Exception {
        return repository.findAll(request);
    }

    @ResponseBody
    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.GET)
    public Greetings get(@PathVariable(value = "id") Long id) {
        return findGreeting(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public Greetings post(@RequestBody(required = false) Greetings greetings) {
        String name = findNameOrDefaultName(greetings);
        return createGreeting(String.format(TEMPLATE, name));
    }

    // TODO voir si PATCH est bien implémenté
    @RequestMapping(value = "/{id:[0-9]+}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    @ResponseBody
    public Greetings put(@PathVariable(value = "id") Long id, @RequestParam(value = "name") String name) {
        findGreeting(id);
        return null; // TODO mise à jour d'une Entity
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public Greetings delete(@PathVariable(value = "id") Long id) {
        Greetings result = repository.findOne(id);
        repository.delete(result);
        if(result == null) {
            throw buildNotFoundException(id);
        }
        return result;
    }

    private Greetings findGreeting(Long id) {
        Optional<Greetings> op = Optional.ofNullable(repository.findOne(id));
        return op.orElseThrow(() -> buildNotFoundException(id));
    }

    // TODO à externaliser dans un service
    private Greetings createGreeting(String name) {
        Objects.requireNonNull(name, "The name must not be null.");
        Greetings greetings = new Greetings(name);
        repository.save(greetings);
        return greetings;
    }

    private String findNameOrDefaultName(@RequestBody(required = false) Greetings greetings) {
        String name = DEFAULT_NAME;
        if(greetings != null && greetings.getName() != null) {
            name = greetings.getName();
        }
        return name;
    }

    private NotFoundException buildNotFoundException(Long id) {
        return new NotFoundException(String.format("Greetings %d not found.", id));
    }
}