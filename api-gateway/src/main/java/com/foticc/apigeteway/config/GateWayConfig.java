package com.foticc.apigeteway.config;

import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class GateWayConfig{

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", predicateSpec -> predicateSpec.path("/auth/**").filters(f -> f.stripPrefix(1)).uri("lb://api-auth"))
                .route("upms", predicateSpec -> predicateSpec.path("/upms/**").filters(f -> f.stripPrefix(1)).uri("lb://api-upms"))
                .build();
    }

}
