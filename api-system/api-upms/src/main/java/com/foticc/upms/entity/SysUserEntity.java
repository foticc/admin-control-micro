package com.foticc.upms.entity;


import com.foticc.upms.entity.base.BaseEntity;
import jakarta.persistence.Entity;


@Entity(name = "sys_user")
public class SysUserEntity extends BaseEntity {

    private String username;

    private String password;

    private Boolean accountExpired = false;

    private Boolean accountLocked = false;

    private Boolean enable = true;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
