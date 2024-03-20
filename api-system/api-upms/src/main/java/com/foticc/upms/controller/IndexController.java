package com.foticc.upms.controller;


import com.foticc.upms.apiclient.ConfigServerClient;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Value("${spring.application.name}")
    private String application;

    private final ConfigServerClient configServerClient;

    private final Long port;

    public IndexController(ConfigServerClient configServerClient, @Value("${server.port}") Long port) {
        this.configServerClient = configServerClient;
        this.port = port;
    }

    @GetMapping("/app")
    public String app(HttpServletRequest request) {
        log.info("app-{}>{}", request.getRequestURI(), request.getRequestedSessionId());
        return this.application + configServerClient.data();
    }

    @GetMapping("/test")
    public String test() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }


    @GetMapping("/scope")
    public List<String> scope() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    @GetMapping("/port")
    public String port() {
        return this.port.toString();
    }

}
