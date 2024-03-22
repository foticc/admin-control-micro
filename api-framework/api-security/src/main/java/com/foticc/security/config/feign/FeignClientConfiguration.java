package com.foticc.security.config.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor oauthRequestInterceptor() {
        return new TestRequestInterceptor();
    }
}
