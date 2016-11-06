package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.dao.ApiDao;
import com.dbalthassat.restapi.exception.clientError.notFound.ResourceNotFoundException;
import com.dbalthassat.restapi.service.GenericService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
		throw new ResourceNotFoundException(method, uri);
	}

	@RequestMapping(value = "/*")
	public List<? extends ApiDao> findAll(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findAll(uri);
	}

	@RequestMapping(value = "/*/{id:[1-9][0-9]*}")
	public ApiDao findOne(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findOne(uri);
	}

	@RequestMapping(value = "/*/{parentId:[1-9][0-9]*}/*")
	public List<? extends ApiDao> findAllWithParent(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findAllWithParent(uri);
	}

	@RequestMapping(value = "/*/{parentId:[1-9][0-9]*}/*/{id:[1-9][0-9]*}")
	public ApiDao test3(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return service.findOneWithParent(uri);
	}

	@RequestMapping(value = "/**")
	public void unmappedRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String method = request.getMethod();
		throw new ResourceNotFoundException(method, uri);
	}
}
