package ru.sauvest.logging.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class MdcFilter extends OncePerRequestFilter {

    private static final String X_REQUEST_ID = "X-Request-Id";

    private static final Logger LOGGER = LoggerFactory.getLogger(MdcFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String xRequestId = request.getHeader(X_REQUEST_ID);
        if (StringUtils.isEmpty(xRequestId)) {
            xRequestId = UUID.randomUUID().toString();
        }

        LOGGER.info("Request: method={}, uri={}, headers={}",
                request.getMethod(),
                request.getRequestURI(),
                getHeaders(request));
        MDC.put(X_REQUEST_ID, xRequestId);
        response.setHeader(X_REQUEST_ID, xRequestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(X_REQUEST_ID);
        }
    }

    private String getHeaders(HttpServletRequest request) {
        // Форматируем заголовки для лога
        return Collections.list(request.getHeaderNames())
                .stream()
                .map(name -> name + "=" + request.getHeader(name))
                .collect(Collectors.joining(", "));
    }

}
