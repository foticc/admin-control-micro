package com.foticc.security.support.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuthUserPrincipal implements OAuth2AuthenticatedPrincipal {


    private final Map<String, Object> attributes = new HashMap<>();

    private final UserDetails userDetails;

    public AuthUserPrincipal(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userDetails.getAuthorities();
    }

    @Override
    public String getName() {
        return this.userDetails.getUsername();
    }
}
