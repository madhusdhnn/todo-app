package com.company.api.todoservice.controllers.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    @Value(value = "${api_key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String apiKeyFromRequest = request.getHeader("X-API-KEY");
        if (isEmpty(apiKeyFromRequest) || !apiKey.equals(apiKeyFromRequest))
            response.setStatus(UNAUTHORIZED.value());
        else chain.doFilter(request, response);
    }
}
