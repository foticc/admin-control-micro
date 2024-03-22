package com.foticc.security.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class TestRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("\"哈哈哈哈\" = " + "哈哈哈哈");
        Map<String, Collection<String>> headers = requestTemplate.headers();
        headers.forEach((k,v)->{
            System.out.println("k = " + k);
            String collect = v.stream().collect(Collectors.joining(","));
            System.out.println("v = " + collect);
        });
    }
}
