package com.foticc.auth.manager;

import com.foticc.auth.extension.constant.ExtensionConstant;
import com.foticc.security.support.principal.RoleGrantedAuthority;
import com.foticc.security.support.principal.UserDetailAuthentication;
import com.foticc.upms.client.dto.SysAuthUserDTO;
import com.foticc.upms.client.feign.RemoteUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsManager implements UserDetailsService {

    private final RedisTemplate<String,Object> jsonRedisTemplate;


    private final RemoteUserService remoteUserService;

    public UserDetailsManager(@Qualifier("jsonRedisTemplate") RedisTemplate<String,Object> jsonRedisTemplate,
                             RemoteUserService remoteUserService) {
        this.jsonRedisTemplate = jsonRedisTemplate;
        this.remoteUserService = remoteUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         UserDetailAuthentication o = (UserDetailAuthentication) jsonRedisTemplate.opsForValue().get(buildKey(username));
        if (o != null) {
            return o;
        }
        SysAuthUserDTO userAuth = remoteUserService.loadUserByUsername(username).getData();
        if (userAuth == null) {
            throw new UsernameNotFoundException("not found");
        }
        if (!userAuth.getEnable()) {
            throw new UsernameNotFoundException("account not enable");
        }
        if (userAuth.getAccountLocked()) {
            throw new UsernameNotFoundException("account locked");
        }


        UserDetailAuthentication user = new UserDetailAuthentication(
                userAuth.getUsername(),
                userAuth.getPassword(),
                userAuth.getAccountExpired(),
                userAuth.getAccountLocked(),
                userAuth.getEnable(),
                Collections.singleton(new RoleGrantedAuthority("admin")));
        jsonRedisTemplate.opsForValue().set(buildKey(username),user);
        return user;
    }

    private String buildKey(String username) {
        return String.format("%s:%s", ExtensionConstant.CACHE_USER_PREFIX,username);
    }
}
