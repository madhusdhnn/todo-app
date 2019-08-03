package com.company.api.todoservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERN = "/todoapp/api/**";
    private static final String[] METHODS = {"PUT", "POST", "PATCH", "DELETE", "GET"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping(PATH_PATTERN)
                .allowedOrigins("*")
                .allowedHeaders(METHODS)
                .allowCredentials(false);
    }
}