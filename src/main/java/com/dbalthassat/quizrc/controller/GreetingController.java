package com.dbalthassat.quizrc.controller;

import com.dbalthassat.quizrc.entity.Greeting;
import com.dbalthassat.quizrc.exception.NotFoundException;
import com.dbalthassat.quizrc.utils.ParamsUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/greetings")
// TODO versioning, cf http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
public class GreetingController {
    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    /**
     * Simule une base de données
      */
    private final Map<Long, Greeting> bdd = new HashMap <>();

    @PostConstruct
    public void postConstruct() {
        List<Greeting> greetings = new LinkedList<>();
        greetings.add(new Greeting(counter.incrementAndGet(), "world"));
        greetings.add(new Greeting(counter.incrementAndGet(), "tata"));
        greetings.add(new Greeting(counter.incrementAndGet(), "toto"));
        greetings.add(new Greeting(counter.incrementAndGet(), "titi"));
        greetings.forEach(e -> bdd.put(e.getId(), e));
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    @ResponseBody
    public List<Greeting> get(HttpServletRequest request) {
        return ParamsUtils.apply(request.getParameterMap(), new LinkedList<>(bdd.values()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Greeting get(@PathVariable(value = "id") Long id) {
        return findGreeting(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Greeting post(@RequestBody(required = false) Greeting greeting) {
        String name = "world";
        if(greeting != null) {
            name = greeting.getName();
        }
        return createOrUpdateGreeting(counter.incrementAndGet(), String.format(template, name));
    }

    // TODO voir si PATCH est bien implémenté
    @RequestMapping(value = "/{id}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    @ResponseBody
    public Greeting put(@PathVariable(value = "id") Long id, @RequestParam(value = "name") String name) {
        findGreeting(id);
        return createOrUpdateGreeting(id, String.format(template, name));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Greeting delete(@PathVariable(value = "id") Long id) {
        findGreeting(id);
        return bdd.remove(id);
    }

    private Greeting findGreeting(Long id) {
        Optional<Greeting> op = Optional.ofNullable(bdd.get(id));
        return op.orElseThrow(NotFoundException::new);
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