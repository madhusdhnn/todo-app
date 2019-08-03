package com.company.api.todoservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class PingController {

    private static final Logger logger = LogManager.getLogger(RestController.class);

    @GetMapping(value = "/")
    public ResponseEntity hello() {
        logger.info(String.format("Pinged at %s", OffsetDateTime.now()));
        return ResponseEntity.ok("Hello World");
    }
}
