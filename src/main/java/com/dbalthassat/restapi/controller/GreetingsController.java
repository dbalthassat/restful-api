package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.exception.ExceptionMessage;
import com.dbalthassat.restapi.exception.clientError.NotFoundException;
import com.dbalthassat.restapi.service.GreetingsService;
import com.dbalthassat.restapi.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/greetings")
// TODO versioning, cf http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
// TODO add an alias
// TODO rate limiting with authenticated user
public class GreetingsController {
    private static final String TEMPLATE = "Hello, %s!";

    private final GreetingsService service;

    @Autowired
    public GreetingsController(GreetingsService service) {
        this.service = service;
    }

    @PostConstruct
    public void initBdd() {
        List<Greetings> greetings = new LinkedList<>();
        greetings.add(new Greetings("world"));
        greetings.add(new Greetings("tata"));
        greetings.add(new Greetings("toto", "A small description"));
        greetings.add(new Greetings("a", "B"));
        greetings.add(new Greetings("titi"));
        service.save(greetings);
    }

    @ResponseBody
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public Iterable<Greetings> get(HttpServletRequest request) throws Exception {
        return service.findAll(request);
    }

    @ResponseBody
    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.GET)
    public Greetings get(@PathVariable(value = "id") Long id) {
        return service.findOne(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Greetings> post(HttpServletRequest request, @Valid @RequestBody(required = false) Greetings greetings) {
        greetings = service.createGreeting(String.format(TEMPLATE, greetings.getName()));
        return HttpUtils.buildPostResponse(request, greetings);
    }

    // TODO voir si PATCH est bien implémenté
    @RequestMapping(value = "/{id:[0-9]+}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    @ResponseBody
    public Greetings put(@PathVariable(value = "id") Long id, @RequestParam(value = "name") String name) {
        service.findOne(id);
        return null; // TODO mise à jour d'une Entity
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public Greetings delete(@PathVariable(value = "id") Long id) {
        Greetings result = service.findOne(id);
        service.delete(result);
        if(result == null) {
            throw new NotFoundException(ExceptionMessage.GREETINGS_NOT_FOUND.getMessage(), id);
        }
        return result;
    }
}