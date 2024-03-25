package com.foticc.upms.service;

import com.foticc.upms.client.dto.SysAuthUserDTO;
import com.foticc.upms.entity.SysUserEntity;
import com.foticc.upms.repos.SysUserEntityRepository;
import org.springframework.data.domain.Example;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final SysUserEntityRepository sysUserEntityRepository;

    public LoginService(SysUserEntityRepository sysUserEntityRepository) {
        this.sysUserEntityRepository = sysUserEntityRepository;
    }

    @Nullable
    public SysAuthUserDTO loadUserByUsername(String username) {
        return sysUserEntityRepository.findByUsername(username).map(
                m->{
                    SysAuthUserDTO sysAuthUserDTO = new SysAuthUserDTO();
                    sysAuthUserDTO.setUsername(m.getUsername());
                    sysAuthUserDTO.setPassword(m.getPassword());
                    sysAuthUserDTO.setEnable(m.getEnable());
                    sysAuthUserDTO.setAccountLocked(m.getAccountLocked());
                    sysAuthUserDTO.setAccountExpired(m.getAccountExpired());
                    return sysAuthUserDTO;
                }
        ).orElse(null);
    }

}
