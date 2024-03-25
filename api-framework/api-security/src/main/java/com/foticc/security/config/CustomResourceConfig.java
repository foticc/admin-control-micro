package com.foticc.security.config;

import com.foticc.security.support.handler.CacheTokenIntrospector;
import com.foticc.security.support.handler.CustomBearerTokenResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

@AutoConfiguration(after = WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(PermitUrlProperties.class)
public class CustomResourceConfig {

    @Bean(name = "cacheTokenIntrospector")
    public OpaqueTokenIntrospector cacheTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        return new CacheTokenIntrospector(authorizationService);
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver(PermitUrlProperties permitUrlProperties) {
        return new CustomBearerTokenResolver(permitUrlProperties);
    }


}
