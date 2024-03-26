package com.foticc.upms.service;

import com.foticc.upms.client.dto.SysAuthUserDTO;
import com.foticc.upms.entity.SysRoleEntity;
import com.foticc.upms.entity.SysUserDetailEntity;
import com.foticc.upms.entity.SysUserEntity;
import com.foticc.upms.repos.SysUserDetailEntityRepository;
import com.foticc.upms.repos.SysUserEntityRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final SysUserEntityRepository userEntityRepository;

    private final SysUserDetailEntityRepository userDetailEntityRepository;

    public LoginService(SysUserEntityRepository sysUserEntityRepository,
                        SysUserDetailEntityRepository userDetailEntityRepository) {
        this.userEntityRepository = sysUserEntityRepository;
        this.userDetailEntityRepository = userDetailEntityRepository;
    }

    @Nullable
    public SysAuthUserDTO loadUserByUsername(String username) {
        Optional<SysUserEntity> byUsername = userEntityRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            return null;
        }
        Optional<SysUserDetailEntity> userDetail = userDetailEntityRepository.findByUsername(username);
        Set<String> roles = userDetail.map(sysUserDetailEntity -> sysUserDetailEntity.getRoles().stream().
                map(SysRoleEntity::getName)
                .collect(Collectors.toSet()))
                .orElse(null);
        return userEntityRepository.findByUsername(username).map(
                m->{
                    SysAuthUserDTO sysAuthUserDTO = new SysAuthUserDTO();
                    sysAuthUserDTO.setUsername(m.getUsername());
                    sysAuthUserDTO.setPassword(m.getPassword());
                    sysAuthUserDTO.setEnable(m.getEnable());
                    sysAuthUserDTO.setAccountLocked(m.getAccountLocked());
                    sysAuthUserDTO.setAccountExpired(m.getAccountExpired());
                    sysAuthUserDTO.setRoles(roles);
                    return sysAuthUserDTO;
                }
        ).orElse(null);
    }

}
