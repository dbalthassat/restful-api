package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.exception.clientError.badRequest.IdMustBeNumericException;
import com.dbalthassat.restapi.service.GreetingsService;
import com.dbalthassat.restapi.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/greetings")
// TODO versioning, cf http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
// TODO rate limiting with authenticated user
public class GreetingsController {
    static final String TEMPLATE = "Hello, %s!";

    private final GreetingsService service;

    @Autowired
    public GreetingsController(GreetingsService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public Iterable<Greetings> get(HttpServletRequest request) throws Exception {
        return service.findAll(request);
    }

    @ResponseBody
    @RequestMapping(value = {"/recentlyCreated"}, method = RequestMethod.GET)
    public Iterable<Greetings> getRecentlyCreated(HttpServletRequest request) throws Exception {
        return service.findAll(request);
    }

    @ResponseBody
    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.GET)
    public Greetings get(@PathVariable(value = "id") Long id) {
        return service.findOne(id);
    }

    @SuppressWarnings("MVCPathVariableInspection")
    @ResponseBody
    @RequestMapping(value = "/{id:(?![0-9]+).*}", method = RequestMethod.GET)
    public Greetings getBadRequest() {
        throw new IdMustBeNumericException();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Greetings> post(HttpServletRequest request, @Valid @RequestBody Greetings greetings) {
        greetings.setName(String.format(TEMPLATE, greetings.getName()));
        greetings = service.createGreeting(greetings);
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
        return service.delete(id);
    }
}