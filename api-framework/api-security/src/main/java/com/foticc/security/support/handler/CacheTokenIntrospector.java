package com.foticc.security.support.handler;

import com.foticc.security.support.principal.AuthUserPrincipal;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.Collections;
import java.util.Objects;

public class CacheTokenIntrospector implements OpaqueTokenIntrospector {


    private final OAuth2AuthorizationService authorizationService;

    public CacheTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
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
        AuthUserPrincipal authUserPrincipal = null;
        // TODO 暂时先从缓存中取到,
        if (authorization.getAttributes().containsKey(Principal.class.getName())) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = authorization.getAttribute(Principal.class.getName());
            UserDetails principal = (UserDetails) Objects.requireNonNull(usernamePasswordAuthenticationToken).getPrincipal();
            // 是否需要重新登录
            // TODO 处理账号被锁定，过期，未启用的状态  （过期或锁定时要删除缓存，这种情况在认证中心也要进行处理）
            authUserPrincipal = new AuthUserPrincipal(principal);
            authUserPrincipal.getAttributes().put(OAuth2ParameterNames.CLIENT_ID,authorization.getRegisteredClientId());
        }

        return authUserPrincipal;
    }
}
