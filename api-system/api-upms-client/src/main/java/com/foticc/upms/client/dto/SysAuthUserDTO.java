package com.foticc.upms.client.dto;

import lombok.Data;

@Data
public class SysAuthUserDTO {

    private String username;

    private String password;

    private Boolean accountExpired;

    private Boolean accountLocked;

    private Boolean enable;

}
