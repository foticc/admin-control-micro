package com.foticc.auth.oauth2.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class ClientVO implements Serializable {


    private String id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;
    private String clientAuthenticationMethods;
    private String authorizationGrantTypes;
    private String redirectUris;
    private String postLogoutRedirectUris;
    private String scopes;
//    private String clientSettings;
    private Boolean requireProofKey;
    private Boolean requireAuthorizationConsent;
    private String jwkSetUrl;
    private String authenticationSigningAlgorithm;
    private String x509CertificateSubjectDN;
//    private String tokenSettings;
    private String authorizationCodeTimeToLive;
    private String accessTokenFormat;
    private String deviceCodeTimeToLive;
    private Boolean reuseRefreshTokens;
    private String refreshTokenTimeToLive;
    private String idTokenSignatureAlgorithm;
    private Boolean x509CertificateBoundAccessTokens;

}
