package com.test.urlshortner.Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class LogginFilter extends OncePerRequestFilter implements Filter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info(request.getRequestURL().toString());
        logger.info(request.getMethod());
        logger.info(request.getParameterMap().values().toString());
        filterChain.doFilter(request, response);
    }

}
