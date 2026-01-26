package ru.otus.hw.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class MdcLogSetupFilter extends OncePerRequestFilter {

    public static final String MDC_REQUEST_ID = "requestId";

    private final Logger log = LoggerFactory.getLogger(MdcLogSetupFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var guid = java.util.UUID.randomUUID().toString();
        MDC.put(MDC_REQUEST_ID, guid);
        log.info("Request: method:{}, uri:{}, clientIp:{}",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        filterChain.doFilter(request, response);
        MDC.remove(MDC_REQUEST_ID);
    }
}
