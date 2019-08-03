package com.company.api.todoservice.controllers;

import com.company.api.todoservice.exceptions.TodoGroupNotFoundException;
import com.company.api.todoservice.exceptions.TodoNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
@RequestMapping(value = "/todoapp")
public class BaseController {

    private static final Logger logger = LogManager.getLogger(BaseController.class);

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> sendTodoNotFoundExceptionMessage(TodoNotFoundException ex) {
        logger.error(String.format("%s", ex.getMessage()));
        return Collections.singletonMap("message", ex.getMessage());
    }

    @ExceptionHandler(TodoGroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> sendTodoGroupNotFoundExceptionMessage(TodoGroupNotFoundException ex) {
        logger.error(String.format("%s", ex.getMessage()));
        return Collections.singletonMap("message", ex.getMessage());
    }

}
