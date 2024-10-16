package com.foticc.apigeteway.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    private final Schedule schedule;

    public TestController(Schedule schedule) {
        this.schedule = schedule;
    }

    @GetMapping("/test")
    public NacosValBean test() {
        return this.schedule.getNacosValBean();
    }
}
