package com.foticc.security.config;

import com.foticc.security.support.handler.CacheTokenIntrospector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

public class CustomResourceConfig {

    @Bean(name = "cacheTokenIntrospector")
    public OpaqueTokenIntrospector cacheTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        return new CacheTokenIntrospector(authorizationService);
    }


}
