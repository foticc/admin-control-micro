package com.foticc.upms.entity;

import com.foticc.upms.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "sys_permission")
public class SysPermissionEntity extends BaseEntity {

    private String path;


    @ManyToMany
    @JoinTable(name = "sys_role_permissions",
        joinColumns = {@JoinColumn(name = "permissions_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<SysRoleEntity> roles = new HashSet<>();

    public Set<SysRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRoleEntity> roles) {
        this.roles = roles;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
