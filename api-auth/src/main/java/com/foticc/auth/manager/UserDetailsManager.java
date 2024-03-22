package com.foticc.auth.manager;

import com.foticc.auth.extension.constant.ExtensionConstant;
import com.foticc.auth.manager.service.SysUserService;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsManager implements UserDetailsService {

    private final RedisTemplate<String,Object> redisTemplate;

    private final SysUserService sysUserService;

    public UserDetailsManager(RedisTemplate<String,Object> redisTemplate,SysUserService sysUserService) {
        this.redisTemplate = redisTemplate;
        this.sysUserService = sysUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails o = (UserDetails) redisTemplate.opsForValue().get(buildKey(username));
        if (o != null) {
            return o;
        }
        UserDetails userAuth = sysUserService.loadSysUser(username);
        if (userAuth == null) {
            throw new UsernameNotFoundException("not found");
        }
        if (!userAuth.isEnabled()) {
            throw new UsernameNotFoundException("account not enable");
        }
        if (!userAuth.isAccountNonExpired()) {
            throw new UsernameNotFoundException("account locked");
        }
        redisTemplate.opsForValue().set(buildKey(username),userAuth);
        return userAuth;
    }

    private String buildKey(String username) {
        return String.format("%s:%s", ExtensionConstant.CACHE_USER_PREFIX,username);
    }
}
