package com.dbalthassat.restapi.utils;

import com.dbalthassat.restapi.entity.GenericEntity;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpUtils {
    private static final Logger LOGGER = getLogger(HttpUtils.class);

    private HttpUtils() {}

    public static <ENTITY extends GenericEntity> ResponseEntity<ENTITY> buildPostResponse(HttpServletRequest request, ENTITY entity) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            responseHeaders.setLocation(new URI(request.getRequestURL() + "/" + Long.toString(entity.getId())));
        } catch (URISyntaxException e) {
            // Should not happen.
            LOGGER.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(entity, responseHeaders, HttpStatus.CREATED);
    }

    public static void buildCountHeader(HttpServletResponse response, List<?> content) {
        response.setHeader("X-Total-Count", Integer.toString(content.size()));
    }

    public static void buildLinkHeader(HttpServletRequest request, HttpServletResponse response, int currentPage, int currentSize, Page<?> result) {
        StringJoiner header = new StringJoiner(", ");
        URL url = StringUtils.createURLExcludingGetParams(request, Arrays.asList("page", "size"));
        String linkPrefix = url.getURL() + (url.getCountParams() == 0 ? "?" : "");
        if(currentPage != 1) {
            header.add("<" + linkPrefix + "page=1&size=" + currentSize + ">; rel=\"first\"");
        }
        if(currentPage > 2 && currentPage <= result.getTotalPages()) {
            header.add("<" + linkPrefix + "page=" + (currentPage - 1) + "&size=" + currentSize + ">; rel=\"prev\"");
        }
        if(currentPage >= 1 && currentPage < result.getTotalPages() - 1) {
            header.add("<" + linkPrefix + "page=" + (currentPage + 1) + "&size=" + currentSize + ">; rel=\"next\"");
        }
        if(currentPage != result.getTotalPages()) {
            header.add("<" + linkPrefix + "page=" + result.getTotalPages() + "&size=" + currentSize + ">; rel=\"last\"");
        }
        response.setHeader("Link", header.toString());
    }
}
