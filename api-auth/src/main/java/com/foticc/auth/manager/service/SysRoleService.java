package com.foticc.auth.manager.service;

import com.foticc.auth.manager.entity.SysRole;
import com.foticc.auth.manager.repos.SysRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService {

    private final SysRoleRepository sysRoleRepository;

    public SysRoleService(SysRoleRepository sysRoleRepository) {
        this.sysRoleRepository = sysRoleRepository;
    }


    public List<SysRole> getRolesByUserId(Long userId) {
        return sysRoleRepository.findByUserId(userId);
    }
}
