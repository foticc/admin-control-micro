package com.foticc.auth.config;

import com.foticc.auth.extension.password.PasswordGrantAuthenticationConverter;
import com.foticc.auth.extension.password.PasswordGrantAuthenticationProvider;
import com.foticc.auth.manager.UserDetailsManager;
import com.foticc.upms.client.feign.RemoteUserService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.ClassUtils;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

/**
 * @see <a href="https://docs.spring.io/spring-authorization-server/reference/guides/how-to-ext-grant-type.html">...</a>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true,securedEnabled = true)
public class AuthorizationServerConfig {


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   OAuth2AuthorizationService oAuth2AuthorizationService,
                                                   OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator
                                                   ) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(new Customizer<OAuth2TokenEndpointConfigurer>() {
                    @Override
                    public void customize(OAuth2TokenEndpointConfigurer tokenEndpointConfigurer) {
                        tokenEndpointConfigurer.accessTokenRequestConverter(new PasswordGrantAuthenticationConverter());
                    }
                })
                .oidc(Customizer.withDefaults());
        http //当未登录时访问认证端点时重定向至登录页面
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                ))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()));

        DefaultSecurityFilterChain build = http.build();
        // 加入自定义授权
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        PasswordGrantAuthenticationProvider passwordGrantAuthenticationProvider =
                new PasswordGrantAuthenticationProvider(authenticationManager, oAuth2AuthorizationService, oAuth2TokenGenerator);
        http.authenticationProvider(passwordGrantAuthenticationProvider);

        return build;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(registry ->
                registry.requestMatchers("/assets/**", "/webjars/**","/token/**").permitAll()
                        .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults());

        httpSecurity.oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer.jwt(Customizer.withDefaults()));

        return httpSecurity.build();
    }

//    @Bean
//    @Primary
//    public UserDetailsManager userDetailsManager(RemoteUserService remoteUserService) {
//        return new UserDetailsManager(remoteUserService);
//    }

    // 一个基于内存的用户service
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails build = User.withUsername("user")
//                .password(passwordEncoder.encode("123456"))
//                .roles("admin", "normal")
//                .authorities("app", "web")
//                .build();
//        return new InMemoryUserDetailsManager(build);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 明文 不加密 测试用
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     *     客户端配置，可以使用数据库存储
     *     客户端ClientSettings 说明
     *     requireProofKey pkce流程时必须为true
     *     requireAuthorizationConsent 为true时登录后会跳转授权确认页面
     *     jwkSetUrl 设置客户端jwk的url
     *     tokenEndpointAuthenticationSigningAlgorithm
     *     Sets the JWS algorithm that must be used for signing the JWT used to authenticate the Client
     *     at the Token Endpoint for the private_key_jwt and client_secret_jwt authentication methods.
     */
//    @Bean
//    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
//        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("client-msg")
//                .clientName("客户端")
//                .clientSecret(passwordEncoder.encode("123456"))
//                //客户端认证方式 ，
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
//                // 配置该客户端支持的授权方式
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(new AuthorizationGrantType("password"))
//                // 可跳转的地址
//                .redirectUri("http://spring-oauth-client:8001/token")
//                .redirectUri("http://spring-oauth-client:8001/test")
//                .redirectUri("http://spring-oauth-client:8001/login/oauth2/code/messaging-client-oidc")
//                .redirectUri("http://spring-oauth-client:8001/system/test")
//                .redirectUri("http://www.baidu.com")
//                // scope 可访问的范围
//                .scope(OidcScopes.PROFILE)
//                .scope(OidcScopes.OPENID)
//                // 客户端设置，设置用户需要确认授权
//                .clientSettings(ClientSettings.builder().requireProofKey(true).requireAuthorizationConsent(true).build())
//                // token的相关设置
//                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(24)).refreshTokenTimeToLive(Duration.ofHours(48)).build())
//                .build();
//
//        return new InMemoryRegisteredClientRepository(client);
//    }

//    //TODO 授权信息和授权确认修改
//    /**
//     * 授权信息
//     * 对应表：oauth2_authorization
//     */
//    @Bean
//    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,RedisTemplate redisTemplate, RegisteredClientRepository registeredClientRepository) {
////        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
//        return new RedisOAuth2AuthorizationService(redisTemplate, registeredClientRepository);
//    }

    /**
     * 授权确认
     *对应表：oauth2_authorization_consent
     */
//    @Bean
//    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
//        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
//    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * keytool -genkeypair -alias jwt -keyalg RSA -keystore jwt.jks
     * -validity 365
     * 解释：
     *
     * --genkeypair：生成密钥对。
     * -alias jwt：为密钥对指定一个别名，这里我们使用mykey。
     * -keyalg RSA：指定使用RSA算法生成密钥对。
     * -keystore jwt.jks：指定密钥库的名称和位置，这里我们创建一个名为mykeystore.jks的密钥库。
     * -validity 365：设置密钥对的有效期为365天。
     *
     *  keytool -genkey -alias jwt -keyalg RSA -keysize 2048 -keystore jwt.jks -validity 365
     *
     * @return
     */

    private static KeyPair generateRsaKey() {
//        KeyPair keyPair;
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);
//            keyPair = keyPairGenerator.generateKeyPair();
//        }
//        catch (Exception ex) {
//            throw new IllegalStateException(ex);
//        }
        ClassPathResource classPathResource = new ClassPathResource("keystore/jwt.jks");
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, "admin123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt");
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource());
    }


    /**
     *配置token生成器
     */
    @Bean
    OAuth2TokenGenerator<?> tokenGenerator() {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource()));
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        accessTokenGenerator.setAccessTokenCustomizer(context -> {

        });
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    // oidc id_token
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtEncodingContextOAuth2TokenCustomizer() {
        return new OAuth2TokenCustomizer<JwtEncodingContext>() {
            @Override
            public void customize(JwtEncodingContext context) {
                JwsHeader.Builder jwsHeader = context.getJwsHeader();
                JwtClaimsSet.Builder claims = context.getClaims();
                if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                    claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()));
                    StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
                    claims.claim("sid", standardSessionIdGenerator.generateSessionId());
                }
            }
        };
    }

}
