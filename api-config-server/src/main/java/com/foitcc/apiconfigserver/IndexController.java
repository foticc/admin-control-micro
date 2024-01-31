package com.foitcc.apiconfigserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class IndexController {


    @Value("${data.time}")
    private String data;


    @GetMapping("/data")
    public String data() {
        return this.data;
    }
}
