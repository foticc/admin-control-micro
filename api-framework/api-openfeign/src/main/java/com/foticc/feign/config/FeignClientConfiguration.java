package com.foticc.feign.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor oauthRequestInterceptor() {
        return new TestRequestInterceptor();
    }

    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }
}
