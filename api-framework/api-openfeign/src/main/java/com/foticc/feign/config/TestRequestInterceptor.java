package com.foticc.feign.config;

import com.foitcc.common.security.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class TestRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SecurityConstants.HEADER_FROM,SecurityConstants.HEADER_FROM_INNER);
    }
}
