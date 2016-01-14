package com.dbalthassat.quizrc.controller;

import com.dbalthassat.quizrc.entity.Greeting;
import com.dbalthassat.quizrc.utils.Params;
import com.dbalthassat.quizrc.utils.ParamsUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController(value = "/greetings")
// TODO versioning, cf http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
public class GreetingController {
    private static final String template = "Hello, %s!";

    // TODO vérifier que le AtomicLong est nécessaire.
    private final AtomicLong counter = new AtomicLong();

    /**
     * Simule une base de données
      */
    private final Map<Long, Greeting> bdd = new HashMap <>();

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Greeting> get(HttpServletRequest request) {
        Params<Greeting> params = ParamsUtils.parseParams(request.getParameterMap());
        return ParamsUtils.apply(bdd.values(), params);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Greeting get(@PathVariable(value = "id") Long id) {
        return findGreeting(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Greeting post(@RequestParam(value="name", defaultValue="World") String name) {
        return createOrUpdateGreeting(counter.incrementAndGet(), String.format(template, name));
    }

    // TODO voir si PATCH est bien implémenté
    @RequestMapping(value = "/{id}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    public Greeting put(@PathVariable(value = "id") Long id, @RequestParam(value = "name") String name) {
        findGreeting(id);
        return createOrUpdateGreeting(id, String.format(template, name));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Greeting delete(@PathVariable(value = "id") Long id) {
        findGreeting(id);
        return bdd.remove(id);
    }

    private Greeting findGreeting(@PathVariable(value = "id") Long id) {
        Optional<Greeting> op = Optional.ofNullable(bdd.get(id));
        return op.orElseThrow(IllegalArgumentException::new); // TODO 404
    }

    // TODO à externaliser dans un service
    private Greeting createOrUpdateGreeting(Long id, String name) {
        Objects.requireNonNull(id, "The id must not be null.");
        Objects.requireNonNull(name, "The name must not be null.");
        Greeting greeting = new Greeting(id, name);
        bdd.put(greeting.getId(), greeting);
        return greeting;
    }
}