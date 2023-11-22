package com.zerosx.common.sas.auth;

import com.zerosx.common.base.constants.TokenStoreConstants;
import com.zerosx.common.redis.templete.RedissonOpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.codec.SerializationCodec;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * CustomOAuth2AuthorizationService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-24 23:04
 **/
@Slf4j
public class CustomOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedissonOpService redissonOpService;
    private final SerializationCodec serializationCodec;

    public CustomOAuth2AuthorizationService(RedissonOpService redissonOpService, SerializationCodec serializationCodec) {
        this.redissonOpService = redissonOpService;
        this.serializationCodec = serializationCodec;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        log.debug("OAuth2Authorization.save()");
        Assert.notNull(authorization, "authorization cannot be null");
        // 保存AccessToken
        OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
        Assert.notNull(accessToken, "authorization cannot be null");
        long accessTokenDiffTime = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
        redissonOpService.hPut(TokenStoreConstants.SAS_ACCESS_TOKEN, accessToken.getTokenValue(), authorization, accessTokenDiffTime, serializationCodec);
        // 保存 RefreshToken -刷新Token时使用
        if (Objects.nonNull(authorization.getRefreshToken())) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            long between = ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            redissonOpService.hPut(TokenStoreConstants.SAS_REFRESH_TOKEN, refreshToken.getTokenValue(), authorization, between, serializationCodec);
        }
        // 存储 前端分页
        redissonOpService.zAdd(TokenStoreConstants.SAS_TOKEN_PAGE + authorization.getRegisteredClientId(), accessToken.getTokenValue(),
                (double) accessToken.getExpiresAt().getEpochSecond(), accessTokenDiffTime, serializationCodec);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        log.debug("OAuth2Authorization.remove()");
        if (authorization == null) {
            return;
        }
        String accessToken = authorization.getAccessToken().getToken().getTokenValue();
        redissonOpService.hRemove(TokenStoreConstants.SAS_ACCESS_TOKEN, accessToken, serializationCodec);
        boolean b = redissonOpService.zRem(TokenStoreConstants.SAS_TOKEN_PAGE + authorization.getRegisteredClientId(), accessToken, serializationCodec);
        if (authorization.getRefreshToken() != null) {
            String refreshToken = authorization.getRefreshToken().getToken().getTokenValue();
            redissonOpService.hRemove(TokenStoreConstants.SAS_REFRESH_TOKEN, refreshToken, serializationCodec);
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        log.debug("OAuth2Authorization.findById():{}", id);
        if (StringUtils.isBlank(id)) {
            return null;
        }
        OAuth2Authorization accessTokenOAuth2Authorization = redissonOpService.hGet(TokenStoreConstants.SAS_ACCESS_TOKEN, id, serializationCodec);
        if (accessTokenOAuth2Authorization != null) {
            return accessTokenOAuth2Authorization;
        }
        OAuth2Authorization refreshTokenOAuth2Authorization = redissonOpService.get(TokenStoreConstants.SAS_REFRESH_TOKEN + id, serializationCodec);
        if (refreshTokenOAuth2Authorization != null) {
            return refreshTokenOAuth2Authorization;
        }
        return null;
    }

    /**
     * 自动续期Token
     *
     * @param token     the token credential
     * @param tokenType the {@link OAuth2TokenType token type}
     * @return
     */
    @Override
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        if (StringUtils.isBlank(token) || tokenType == null) {
            return null;
        }
        if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return redissonOpService.hGet(TokenStoreConstants.SAS_ACCESS_TOKEN, token, serializationCodec);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return redissonOpService.hGet(TokenStoreConstants.SAS_REFRESH_TOKEN, token, serializationCodec);
        }
        return null;
    }

}
