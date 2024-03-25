package com.foticc.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class AuthResourceServerConfig {


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          OpaqueTokenIntrospector tokenIntrospector,
                                                          PermitUrlProperties permitUrlProperties
                                                          ) throws Exception {
        List<String> urls = permitUrlProperties.getUrls();
        AntPathRequestMatcher[] ignores = urls
                .stream().map(m -> new AntPathRequestMatcher(m.trim()))
                .toList()
                .toArray(new AntPathRequestMatcher[urls.size()]);

        http.cors(CorsConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(registry -> registry.requestMatchers(ignores).permitAll().anyRequest().authenticated())
                .oauth2ResourceServer(resourceServerConfigurer -> {
                    resourceServerConfigurer.opaqueToken(opaqueTokenConfigurer -> opaqueTokenConfigurer.introspector(tokenIntrospector));
                        }
                );
        return http.build();
    }

}
