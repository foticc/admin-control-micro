package com.foitcc.apiconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class ApiConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiConfigServerApplication.class, args);
    }

}
