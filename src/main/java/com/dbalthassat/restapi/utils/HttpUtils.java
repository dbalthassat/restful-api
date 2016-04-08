package com.dbalthassat.restapi.utils;

import com.dbalthassat.restapi.entity.ApiEntity;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpUtils {
    private static final Logger LOGGER = getLogger(HttpUtils.class);

    private HttpUtils() {}

    public static <ENTITY extends ApiEntity> ResponseEntity<ENTITY> buildPostResponse(HttpServletRequest request, ENTITY entity) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            responseHeaders.setLocation(new URI(request.getRequestURL() + "/" + Long.toString(entity.getId())));
        } catch (URISyntaxException e) {
            // Should not happen.
            LOGGER.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(entity, responseHeaders, HttpStatus.CREATED);
    }
}
