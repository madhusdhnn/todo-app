package com.company.api.todoservice.exceptions;

public class TodoGroupNotFoundException extends RuntimeException {

    public TodoGroupNotFoundException(String message) {
        super(message);
    }
}
