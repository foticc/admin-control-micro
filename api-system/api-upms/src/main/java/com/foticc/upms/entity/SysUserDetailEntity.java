package com.foticc.upms.entity;


import com.foticc.upms.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Set;


@Entity(name = "sys_user_details")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "union_key",columnNames = "auth_id")
})
public class SysUserDetailEntity extends BaseEntity {

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private Byte type;

    private String authId;

    @ManyToMany
    @JoinTable(name = "sys_user_details_roles",
            joinColumns = @JoinColumn(name = "user_details_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<SysRoleEntity> roles = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public Set<SysRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRoleEntity> roles) {
        this.roles = roles;
    }
}
