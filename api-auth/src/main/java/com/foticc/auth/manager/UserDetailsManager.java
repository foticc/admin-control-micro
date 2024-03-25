package com.foticc.auth.manager;

import com.foitcc.common.security.SecurityConstants;
import com.foticc.auth.extension.constant.ExtensionConstant;
import com.foticc.auth.manager.entity.SysUser;
import com.foticc.auth.manager.service.SysRoleService;
import com.foticc.auth.manager.service.SysUserService;
import com.foticc.upms.client.dto.SysAuthUserDTO;
import com.foticc.upms.client.feign.RemoteUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDetailsManager implements UserDetailsService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final SysUserService sysUserService;

    private final SysRoleService sysRoleService;

    private final RemoteUserService remoteUserService;

    public UserDetailsManager(RedisTemplate<String,Object> redisTemplate,
                              SysUserService sysUserService,
                              SysRoleService sysRoleService, RemoteUserService remoteUserService) {
        this.redisTemplate = redisTemplate;
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.remoteUserService = remoteUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysAuthUserDTO userAuth = remoteUserService.loadUserByUsername(username).getData();
//        UserDetails o = (UserDetails) redisTemplate.opsForValue().get(buildKey(username));
//        if (o != null) {
//            return o;
//        }
//        SysUser userAuth = sysUserService.loadSysUser(username);
//        if (userAuth == null) {
//            throw new UsernameNotFoundException("not found");
//        }
//        if (!userAuth.getEnable()) {
//            throw new UsernameNotFoundException("account not enable");
//        }
//        if (userAuth.getAccountLocked()) {
//            throw new UsernameNotFoundException("account locked");
//        }
//        Set<SimpleGrantedAuthority> roles = sysRoleService.getRolesByUserId(userAuth.getId())
//                .stream().map(m-> new SimpleGrantedAuthority(m.getName()))
//                .collect(Collectors.toSet());
//
//        User user = new User(userAuth.getUsername(),
//                userAuth.getPassword(),
//                userAuth.getEnable(),
//                !userAuth.getAccountExpired(),
//                !userAuth.getAccountExpired(),
//                !userAuth.getAccountLocked(),
//                roles);
//        redisTemplate.opsForValue().set(buildKey(username),user);
        return new User(userAuth.getUsername(),
                userAuth.getPassword(),
                userAuth.getEnable(),
                !userAuth.getAccountExpired(),
                !userAuth.getAccountExpired(),
                !userAuth.getAccountLocked(),
                Set.of(new SimpleGrantedAuthority("admin")));
    }

    private String buildKey(String username) {
        return String.format("%s:%s", ExtensionConstant.CACHE_USER_PREFIX,username);
    }
}
