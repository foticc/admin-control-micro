package com.foticc.security.support.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Collections;
import java.util.Objects;

public class CacheTokenIntrospector implements OpaqueTokenIntrospector {


    private final OAuth2AuthorizationService authorizationService;

    private final ApplicationContext applicationContext;

    public CacheTokenIntrospector(ApplicationContext applicationContext,
            OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
        this.applicationContext = applicationContext;
    }

    // TODO 怎么做
    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null) {
            throw new InvalidBearerTokenException(token);
        }
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorization.getAuthorizationGrantType())) {
            return new DefaultOAuth2AuthenticatedPrincipal(authorization.getPrincipalName(),
                    Objects.requireNonNull(authorization.getAccessToken().getClaims()),
                    Collections.emptyList()
                    );
        }

        return null;
    }
}
