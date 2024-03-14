package com.foticc.auth.extension.password;

import com.foticc.auth.extension.constant.ExtensionConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

public class PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public static final AuthorizationGrantType FLAG = new AuthorizationGrantType(ExtensionConstant.GRANT_TYPE_PASSWORD);

    /**
     * Sub-class constructor.
     *
     * @param clientPrincipal        the authenticated client principal
     * @param additionalParameters   the additional parameters
     */
    protected PasswordGrantAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(FLAG, clientPrincipal, additionalParameters);
    }
}
