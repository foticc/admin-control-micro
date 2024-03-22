package com.foticc.auth.extension.password;

import com.foticc.auth.extension.constant.ExtensionConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public static final AuthorizationGrantType FLAG = new AuthorizationGrantType(ExtensionConstant.GRANT_TYPE_PASSWORD);

    // todo 暂时设置默认包含所有oidc搜的scope
    private final Set<String> scopes;

    /**
     * Sub-class constructor.
     *
     * @param clientPrincipal      the authenticated client principal
     * @param additionalParameters the additional parameters
     * @param scopes
     */
    protected PasswordGrantAuthenticationToken(Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(FLAG, clientPrincipal, additionalParameters);
        this.scopes = scopes;
    }


    public Set<String> getScopes() {
        return this.scopes;
    }
}
