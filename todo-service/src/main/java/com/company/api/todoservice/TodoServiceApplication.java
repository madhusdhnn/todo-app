package com.company.api.todoservice;

import com.company.api.todoservice.controllers.filters.ApiTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TodoServiceApplication {

    private final ApiTokenFilter apiTokenFilter;

    @Autowired
    public TodoServiceApplication(ApiTokenFilter apiTokenFilter) {
        this.apiTokenFilter = apiTokenFilter;
    }

    public static void main(String[] args) {
        SpringApplication.run(TodoServiceApplication.class, args);
    }


    @Bean
    public FilterRegistrationBean registerApiTokenFilter() {
        FilterRegistrationBean<ApiTokenFilter> apiTokenFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        apiTokenFilterFilterRegistrationBean.setFilter(apiTokenFilter);
        apiTokenFilterFilterRegistrationBean.addUrlPatterns("/todoapp/api/*");
        apiTokenFilterFilterRegistrationBean.setName("ApiTokenFilter");
        return apiTokenFilterFilterRegistrationBean;
    }

}

