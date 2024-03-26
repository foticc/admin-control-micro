package com.foticc.upms.client.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SysAuthUserDTO {

    private String username;

    private String password;

    private Boolean accountExpired;

    private Boolean accountLocked;

    private Boolean enable;

    private Set<String> roles;

}
