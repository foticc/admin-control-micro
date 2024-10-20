package com.foticc.auth.oauth2.convert;
import java.time.Instant;

import com.foticc.auth.oauth2.domain.ClientVO;
import com.foticc.auth.oauth2.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class ClientConvert {


    public ClientVO toVo(Client client) {
        ClientVO clientVO = new ClientVO();
        clientVO.setId(clientVO.getId());
        clientVO.setClientId(client.getClientId());
        clientVO.setClientIdIssuedAt(client.getClientIdIssuedAt());
        clientVO.setClientSecret(client.getClientSecret());
        clientVO.setClientSecretExpiresAt(client.getClientSecretExpiresAt());
        clientVO.setClientName(clientVO.getClientName());
        clientVO.setClientAuthenticationMethods(client.getClientAuthenticationMethods());
        clientVO.setAuthorizationGrantTypes(clientVO.getAuthorizationGrantTypes());
        clientVO.setRedirectUris(clientVO.getRedirectUris());
        clientVO.setPostLogoutRedirectUris(clientVO.getPostLogoutRedirectUris());
        clientVO.setScopes(clientVO.getScopes());
        clientVO.setRequireProofKey(false);
        clientVO.setRequireAuthorizationConsent(false);
        clientVO.setJwkSetUrl("");
        clientVO.setAuthenticationSigningAlgorithm("");
        clientVO.setX509CertificateSubjectDN("");
        clientVO.setAuthorizationCodeTimeToLive("");
        clientVO.setAccessTokenFormat("");
        clientVO.setDeviceCodeTimeToLive("");
        clientVO.setReuseRefreshTokens(false);
        clientVO.setRefreshTokenTimeToLive("");
        clientVO.setIdTokenSignatureAlgorithm("");
        clientVO.setX509CertificateBoundAccessTokens(false);
        return clientVO;
    }
}
