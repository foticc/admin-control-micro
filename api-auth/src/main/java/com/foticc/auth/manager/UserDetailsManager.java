package com.foticc.auth.manager;

import com.foticc.upms.client.dto.SysAuthUserDTO;
import com.foticc.upms.client.feign.RemoteUserService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

public class UserDetailsManager implements UserDetailsService {


    private final RemoteUserService remoteUserService;

    public UserDetailsManager(RemoteUserService remoteUserService) {
        this.remoteUserService = remoteUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysAuthUserDTO userAuth = remoteUserService.loadUserByUsername(username);
        if (userAuth == null) {
            throw new UsernameNotFoundException("not found");
        }
        if (userAuth.getEnable()) {
            throw new UsernameNotFoundException("account not enable");
        }
        if (userAuth.getAccountLocked()) {
            throw new UsernameNotFoundException("account locked");
        }
        return new User(userAuth.getUsername(),userAuth.getPassword(), Collections.emptyList());
    }
}
