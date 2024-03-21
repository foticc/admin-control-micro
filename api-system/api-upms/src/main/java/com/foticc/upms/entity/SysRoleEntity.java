package com.foticc.upms.entity;

import com.foticc.upms.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Set;


@Entity(name = "sys_role")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_role",columnNames = "name")
})
public class SysRoleEntity extends BaseEntity {

    @Column(unique = true)
    private String name;


    @ManyToMany(mappedBy = "roles")
    private Set<SysUserDetailEntity> users = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<SysPermissionEntity> permissions = new HashSet<>();

    public Set<SysPermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SysPermissionEntity> permissions) {
        this.permissions = permissions;
    }

    public Set<SysUserDetailEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<SysUserDetailEntity> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
