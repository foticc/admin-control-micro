package com.foticc.upms;

import com.foticc.security.annotation.EnableResourceServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableResourceServer
@EnableFeignClients(basePackages = {"com.foticc"})
@EnableDiscoveryClient
@SpringBootApplication
public class ApiUpmsApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ApiUpmsApplication.class, args);
    }

}
