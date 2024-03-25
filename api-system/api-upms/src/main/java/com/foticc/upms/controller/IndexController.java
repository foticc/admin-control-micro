package com.foticc.upms.controller;


import com.foticc.security.annotation.InnerAPI;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/index")
@RestController
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Value("${spring.application.name}")
    private String application;


    private final Long port;

    public IndexController(@Value("${server.port}") Long port) {
        this.port = port;
    }


    @InnerAPI
    @GetMapping("/test/v2/demo")
    @PreAuthorize("hasAuthority('admin')")
    public String test() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

    @InnerAPI
    @GetMapping("/scope")
    public List<String> scope() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    @GetMapping("/port")
    @PreAuthorize("hasRole('ROLE_admin')")
    public String port() {
        return this.port.toString();
    }


}
