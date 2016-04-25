package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.exception.clientError.badRequest.WrongContentTypeException;
import com.dbalthassat.restapi.exception.clientError.notFound.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class DefaultController {
	@RequestMapping(value = "/**")
	public void unmappedRequest(HttpServletRequest request) {
		String contentType = request.getContentType();
		if(!MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
			throw new WrongContentTypeException();
		}
		String uri = request.getRequestURI();
		String method = request.getMethod();
		throw new ResourceNotFoundException(method, uri);
	}
}
