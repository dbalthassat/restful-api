package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.Greeting;
import com.dbalthassat.restapi.exception.NotFoundException;
import com.dbalthassat.restapi.utils.ParamsUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/greetings")
// TODO versioning, cf http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
public class GreetingController {
    private static final String TEMPLATE = "Hello, %s!";
    private static final String DEFAULT_NAME = "world";
    private static final AtomicLong COUNTER = new AtomicLong();

    /**
     * Simule une base de données
      */
    private static final Map<Long, Greeting> BDD = new HashMap<>();

    @PostConstruct
    public void initBdd() {
        List<Greeting> greetings = new LinkedList<>();
        greetings.add(new Greeting(COUNTER.incrementAndGet(), "world"));
        greetings.add(new Greeting(COUNTER.incrementAndGet(), "tata"));
        Greeting greetingWithDescription = new Greeting(COUNTER.incrementAndGet(), "toto");
        greetingWithDescription.setDescription("A small description");
        greetings.add(greetingWithDescription);
        greetings.add(new Greeting(COUNTER.incrementAndGet(), "titi"));
        greetings.forEach(e -> BDD.put(e.getId(), e));
    }

    @ResponseBody
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<Greeting> get(HttpServletRequest request) {
        return ParamsUtils.apply(new HashMap<>(request.getParameterMap()), new LinkedList<>(BDD.values()));
    }

    @ResponseBody
    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.GET)
    public Greeting get(@PathVariable(value = "id") Long id) {
        return findGreeting(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public Greeting post(@RequestBody(required = false) Greeting greeting) {
        String name = findNameOrDefaultName(greeting);
        return createOrUpdateGreeting(COUNTER.incrementAndGet(), String.format(TEMPLATE, name));
    }

    // TODO voir si PATCH est bien implémenté
    @RequestMapping(value = "/{id:[0-9]+}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    @ResponseBody
    public Greeting put(@PathVariable(value = "id") Long id, @RequestParam(value = "name") String name) {
        findGreeting(id);
        return createOrUpdateGreeting(id, String.format(TEMPLATE, name));
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public Greeting delete(@PathVariable(value = "id") Long id) {
        Greeting result = BDD.remove(id);
        if(result == null) {
            throw buildNotFoundException(id);
        }
        return result;
    }

    private Greeting findGreeting(Long id) {
        Optional<Greeting> op = Optional.ofNullable(BDD.get(id));
        return op.orElseThrow(() -> buildNotFoundException(id));
    }

    // TODO à externaliser dans un service
    private Greeting createOrUpdateGreeting(Long id, String name) {
        Objects.requireNonNull(id, "The id must not be null.");
        Objects.requireNonNull(name, "The name must not be null.");
        Greeting greeting = new Greeting(id, name);
        BDD.put(greeting.getId(), greeting);
        return greeting;
    }

    private String findNameOrDefaultName(@RequestBody(required = false) Greeting greeting) {
        String name = DEFAULT_NAME;
        if(greeting != null && greeting.getName() != null) {
            name = greeting.getName();
        }
        return name;
    }

    private NotFoundException buildNotFoundException(Long id) {
        return new NotFoundException(String.format("Greeting %d not found.", id));
    }
}