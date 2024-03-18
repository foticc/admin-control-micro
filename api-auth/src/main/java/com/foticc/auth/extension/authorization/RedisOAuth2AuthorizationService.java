package com.foticc.auth.extension.authorization;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis存储
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final static Long TIMEOUT = 10L;
    private static final String AUTHORIZATION = "token";

    private final RedisTemplate<String,Object> redisTemplate;

    private final RegisteredClientRepository registeredClientRepository;

    public RedisOAuth2AuthorizationService(RedisTemplate<String, Object> redisTemplate,
                                           RegisteredClientRepository registeredClientRepository1) {
        this.redisTemplate = redisTemplate;
        this.registeredClientRepository = registeredClientRepository1;
    }

    @Override
    public void save(OAuth2Authorization authorization) {

        if (hasState(authorization)) {
            String state = authorization.getAttribute("state");
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue()
                    .set(buildKey(OAuth2ParameterNames.STATE, state), authorization, TIMEOUT, TimeUnit.MINUTES);
        }

        if (isCode(authorization)) {
            // 授权码
            OAuth2Authorization.Token<OAuth2AuthorizationCode> grantCode = authorization.getToken(OAuth2AuthorizationCode.class);
            Assert.notNull(grantCode,"grant code is null");
            OAuth2AuthorizationCode token = grantCode.getToken();
            String buildKey = buildKey(OAuth2ParameterNames.CODE, token.getTokenValue());
            long between = ChronoUnit.SECONDS.between(Objects.requireNonNull(token.getIssuedAt()), token.getExpiresAt());
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(buildKey,authorization,between,TimeUnit.SECONDS);
        }

        if (hasRefreshToken(authorization)) {
            OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
            Assert.notNull(refreshToken,"refreshToken is null");
            OAuth2RefreshToken token = refreshToken.getToken();
            String buildKey = buildKey(OAuth2ParameterNames.REFRESH_TOKEN, token.getTokenValue());
            long between = ChronoUnit.SECONDS.between(Objects.requireNonNull(token.getIssuedAt()), token.getExpiresAt());
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(buildKey,authorization,between,TimeUnit.SECONDS);
        }

        if (hasAccessToken(authorization)) {
            OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
            Assert.notNull(accessToken,"accessToken is null");
            OAuth2AccessToken token = accessToken.getToken();
            String buildKey = buildKey(OAuth2ParameterNames.ACCESS_TOKEN, token.getTokenValue());
            long between = ChronoUnit.SECONDS.between(Objects.requireNonNull(token.getIssuedAt()), token.getExpiresAt());
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(buildKey,authorization,between,TimeUnit.SECONDS);
        }

    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        List<String> keys = new ArrayList<>();
        if (hasState(authorization)) {
            String state = authorization.getAttribute("state");
            keys.add(buildKey(OAuth2ParameterNames.STATE, state));
        }

        if (isCode(authorization)) {
            // 授权码
            OAuth2Authorization.Token<OAuth2AuthorizationCode> grantCode = authorization.getToken(OAuth2AuthorizationCode.class);
            Assert.notNull(grantCode,"grant code is null");
            OAuth2AuthorizationCode token = grantCode.getToken();
            String buildKey = buildKey(OAuth2ParameterNames.CODE, token.getTokenValue());
            keys.add(buildKey);
        }

        if (hasRefreshToken(authorization)) {
            OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
            Assert.notNull(refreshToken,"refreshToken is null");
            OAuth2RefreshToken token = refreshToken.getToken();
            String buildKey = buildKey(OAuth2ParameterNames.REFRESH_TOKEN, token.getTokenValue());
            keys.add(buildKey);
        }

        if (hasAccessToken(authorization)) {
            OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
            Assert.notNull(accessToken,"accessToken is null");
            OAuth2AccessToken token = accessToken.getToken();
            String buildKey = buildKey(OAuth2ParameterNames.ACCESS_TOKEN, token.getTokenValue());
            keys.add(buildKey);
        }
        redisTemplate.delete(keys);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        redisTemplate.setValueSerializer(RedisSerializer.java());
        return (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(tokenType.getValue(), token));
    }

    private String buildKey(String type, String id) {
        return String.format("%s:%s:%s", AUTHORIZATION, type, id);
    }

    private static boolean hasState(OAuth2Authorization authorization) {
        return StringUtils.hasText(authorization.getAttribute("state"));
    }

    private static boolean isCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> token = authorization.getToken(OAuth2AuthorizationCode.class);
        return token != null;
    }

    private static boolean hasRefreshToken(OAuth2Authorization authorization) {
        return authorization.getRefreshToken() != null;
    }

    private static boolean hasAccessToken(OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }


}
