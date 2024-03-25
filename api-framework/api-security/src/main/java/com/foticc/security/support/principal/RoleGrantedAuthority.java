package com.foticc.security.support.principal;

import org.springframework.security.core.GrantedAuthority;

public class RoleGrantedAuthority implements GrantedAuthority {

    private String authority;

    public RoleGrantedAuthority() {
    }

    public RoleGrantedAuthority(String role) {
        this.authority = role;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
