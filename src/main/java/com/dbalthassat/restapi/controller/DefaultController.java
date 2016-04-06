package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.exception.clientError.ResourceNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class DefaultController {
	@RequestMapping(value = "/**")
	public void unmappedRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String method = request.getMethod();
		throw new ResourceNotFoundException("Request method %s not supported for path %s", method, uri);
	}
}
