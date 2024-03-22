package com.foticc.auth.manager.service;

import com.foticc.auth.manager.entity.SysUser;
import com.foticc.auth.manager.repos.SysUserRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

@Service
public class SysUserService {

    private final SysUserRepository userRepository;

    public SysUserService(SysUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *     security 中的user 一些字段定义的  是反的
     * @see org.springframework.security.core.userdetails.User
     *  accountNonExpired – set to true if the account has not expired
      */
    @Nullable
    public SysUser loadSysUser(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
