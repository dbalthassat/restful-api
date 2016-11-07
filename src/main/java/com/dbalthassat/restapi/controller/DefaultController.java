package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.exception.clientError.notFound.RequestMethodNotFoundException;
import com.dbalthassat.restapi.service.GenericService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/")
public class DefaultController {
	@Autowired
	private GenericService service;

	private static final Logger LOGGER = getLogger(DefaultController.class);

	@RequestMapping("/")
	public void root(HttpServletRequest request) {
		// TODO documentation (swagger ? comment on fait pour documenter automatiquement des ressources qui ne sont pas explicitement déclarées ?)
		String uri = request.getRequestURI();
		String method = request.getMethod();
		throw new RequestMethodNotFoundException(method, uri);
	}

	@RequestMapping(value = "/*", method = RequestMethod.GET)
	public List<? extends ApiDao> findAll(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findAll(uri);
	}

	@RequestMapping(value = "/{resource:[a-z]+}/first", method = RequestMethod.GET)
	public ApiDao findFirst(@PathVariable("resource") String resource) {
		return service.findFirst(resource);
	}

	@RequestMapping(value = "/{resource:[a-z]+}/{id:[1-9][0-9]*}", method = RequestMethod.GET)
	public ApiDao findOne(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findOne(uri);
	}

	@RequestMapping(value = "/*/{parentId:[1-9][0-9]*}/*", method = RequestMethod.GET)
	public List<? extends ApiDao> findAllWithParent(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findAllWithParent(uri);
	}

	@RequestMapping(value = "/*/{parentId:[1-9][0-9]*}/*/{id:[1-9][0-9]*}", method = RequestMethod.GET)
	public ApiDao test3(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findOneWithParent(uri);
	}

	@RequestMapping(value = "/**")
	public void unmappedRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String method = request.getMethod();
		throw new RequestMethodNotFoundException(method, uri);
	}
}
