package com.foticc.security.config;


import com.foticc.security.support.handler.CacheTokenIntrospector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthResourceServerConfig {


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          OpaqueTokenIntrospector tokenIntrospector) throws Exception {

        http.cors(CorsConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(registry -> registry.anyRequest().authenticated())
                .oauth2ResourceServer(resourceServerConfigurer -> {
                    resourceServerConfigurer.opaqueToken(opaqueTokenConfigurer -> opaqueTokenConfigurer.introspector(tokenIntrospector));
                        }
                );
        return http.build();
    }

}
