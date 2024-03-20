package com.foticc.upms;

import com.foticc.security.annotation.EnableResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableResourceServer
public class ApiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSystemApplication.class, args);
    }

}
