package com.foitcc.apisys.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Value("${spring.application.name}")
    private String application;

    @GetMapping("/app")
    public String app(HttpServletRequest request) {
        log.info("app-{}>{}", request.getRequestURI(), request.getRequestedSessionId());
        return this.application;
    }

}