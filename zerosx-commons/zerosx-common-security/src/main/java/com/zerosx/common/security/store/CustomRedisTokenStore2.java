package com.zerosx.common.security.store;

import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.base.constants.TokenStoreConstants;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.codec.SerializationCodec;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.*;

/**
 * 自定义TokenStore 与RedisTokenStore源码基本一致，不同：
 * 1）CLIENT_ID_TO_ACCESS、UNAME_TO_ACCESS改list结构存储
 * 2）增加自定义配置，如key前缀等等
 * 3）增加token自动续签逻辑
 */
@Slf4j
public class CustomRedisTokenStore2 implements TokenStore {

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    private final RedissonOpService redissonOpService;
    private final CustomSecurityProperties customSecurityProperties;
    private final SerializationCodec serializationCodec;

    public CustomRedisTokenStore2(RedissonOpService redissonOpService, CustomSecurityProperties customSecurityProperties, SerializationCodec serializationCodec) {
        this.redissonOpService = redissonOpService;
        this.customSecurityProperties = customSecurityProperties;
        this.serializationCodec = serializationCodec;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String key = TokenStoreConstants.AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication);
        OAuth2AccessToken accessToken = redissonOpService.get(key, serializationCodec);
        if (accessToken != null) {
            OAuth2Authentication storedAuthentication = readAuthentication(accessToken.getValue());
            if ((storedAuthentication == null || !key.equals(authenticationKeyGenerator.extractKey(storedAuthentication)))) {
                storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication oAuth2Authentication = readAuthentication(token.getValue());
        if (oAuth2Authentication == null) {
            return null;
        }
        Boolean renewToken = customSecurityProperties.getRenewToken();
        if (renewToken) {
            int expiresIn = token.getExpiresIn();//剩余时间
            // 获取过期时长
            int validitySeconds = getAccessTokenValiditySeconds(oAuth2Authentication.getOAuth2Request().getClientId());
            double renewTokenPercent = customSecurityProperties.getRenewTokenPercent() == null ? SecurityConstants.RENEW_TOKEN_PERCENT : customSecurityProperties.getRenewTokenPercent();
            //小于设置百分比或者剩余时间大于有效期(有效期可能更新了)
            if ((expiresIn / (double) validitySeconds <= renewTokenPercent) || (expiresIn > validitySeconds)) {
                // 更新AccessToken过期时间
                DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) token;
                oAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
                storeAccessToken(oAuth2AccessToken, oAuth2Authentication, true);
                log.debug("客户端【{}】Token过期时间剩余{}，执行Token续签逻辑", oAuth2Authentication.getName(), customSecurityProperties.getRenewTokenPercent());
            }
        }
        return oAuth2Authentication;
    }

    /**
     * 获取token的总有效时长
     *
     * @param clientId 应用id
     */
    private int getAccessTokenValiditySeconds(String clientId) {
        String clientDetailsClientId = RedisKeyNameEnum.key(RedisKeyNameEnum.OAUTH_CLIENT_DETAILS, clientId);
        ClientDetails object = redissonOpService.get(clientDetailsClientId);
        if (object == null) {
            return SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS;
        }
        return object.getAccessTokenValiditySeconds();
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        return redissonOpService.get(TokenStoreConstants.AUTH + token, serializationCodec);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        return redissonOpService.get(TokenStoreConstants.REFRESH_AUTH + token, serializationCodec);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        storeAccessToken(token, authentication, false);
    }

    public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication authentication, boolean renewToken) {
        String accessKey = TokenStoreConstants.ACCESS + oAuth2AccessToken.getValue();
        String authKey = TokenStoreConstants.AUTH + oAuth2AccessToken.getValue();
        String authToAccessKey = TokenStoreConstants.AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication);
        String approvalKey = TokenStoreConstants.UNAME_TO_ACCESS + getApprovalKey(authentication);
        String clientId = TokenStoreConstants.CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId();

        OAuth2AccessToken previousAccessToken = redissonOpService.get(accessKey, serializationCodec);
        // 如果token已存在，并且不是续签的话直接返回
        if (!renewToken && previousAccessToken != null) {
            log.debug("客户端【{}】token已存在，且不是续签token", authentication.getName());
            return;
        }
        redissonOpService.set(accessKey, oAuth2AccessToken, serializationCodec);
        redissonOpService.set(authKey, authentication, serializationCodec);
        redissonOpService.set(authToAccessKey, oAuth2AccessToken, serializationCodec);
        // 如果是续签token，需要先删除集合里旧的值
        if (previousAccessToken != null) {
            if (!authentication.isClientOnly()) {
                redissonOpService.zRem(approvalKey, oAuth2AccessToken.getValue(), serializationCodec);
            }
            redissonOpService.zRem(clientId, oAuth2AccessToken.getValue(), serializationCodec);
        }
        if (!authentication.isClientOnly()) {
            redissonOpService.zAdd(approvalKey, oAuth2AccessToken.getValue(), (double) oAuth2AccessToken.getExpiration().getTime(), serializationCodec);
        }
        redissonOpService.zAdd(clientId, oAuth2AccessToken.getValue(), (double) oAuth2AccessToken.getExpiration().getTime(), serializationCodec);
        if (oAuth2AccessToken.getExpiration() != null) {
            int seconds = oAuth2AccessToken.getExpiresIn();
            redissonOpService.expire(accessKey, seconds, serializationCodec);
            redissonOpService.expire(authKey, seconds, serializationCodec);
            redissonOpService.expire(authToAccessKey, seconds, serializationCodec);
            redissonOpService.expire(clientId, seconds, serializationCodec);
            redissonOpService.expire(approvalKey, seconds, serializationCodec);
        }
        OAuth2RefreshToken refreshToken = oAuth2AccessToken.getRefreshToken();
        if (refreshToken != null && refreshToken.getValue() != null) {
            String refreshToAccessKey = TokenStoreConstants.REFRESH_TO_ACCESS + refreshToken.getValue();
            String accessToRefreshKey = TokenStoreConstants.ACCESS_TO_REFRESH + oAuth2AccessToken.getValue();
            redissonOpService.set(refreshToAccessKey, oAuth2AccessToken.getValue(), serializationCodec);
            redissonOpService.set(accessToRefreshKey, refreshToken.getValue(), serializationCodec);
            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                Date expiration = expiringRefreshToken.getExpiration();
                if (expiration != null) {
                    int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
                    redissonOpService.expire(refreshToAccessKey, seconds, serializationCodec);
                    redissonOpService.expire(accessToRefreshKey, seconds, serializationCodec);
                }
            }
        }
    }

    private static String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private static String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        if (accessToken == null) {
            return;
        }
        removeAccessToken(accessToken.getValue());
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        return redissonOpService.get(TokenStoreConstants.ACCESS + tokenValue, serializationCodec);
    }

    public void removeAccessToken(String tokenValue) {
        String accessKey = TokenStoreConstants.ACCESS + tokenValue;
        String authKey = TokenStoreConstants.AUTH + tokenValue;
        String accessToRefreshKey = TokenStoreConstants.ACCESS_TO_REFRESH + tokenValue;
        //Object accessKeyObj = redissonOpService.get(accessKey);
        OAuth2Authentication authentication = redissonOpService.get(authKey, serializationCodec);
        redissonOpService.del(accessKey, serializationCodec);
        redissonOpService.del(accessToRefreshKey, serializationCodec);
        // Don't remove the refresh token - it's up to the caller to do that
        redissonOpService.del(authKey, serializationCodec);
        if (authentication != null) {
            String key = authenticationKeyGenerator.extractKey(authentication);
            String authToAccessKey = TokenStoreConstants.AUTH_TO_ACCESS + key;
            String unameKey = TokenStoreConstants.UNAME_TO_ACCESS + getApprovalKey(authentication);
            String clientId = TokenStoreConstants.CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId();
            redissonOpService.del(authToAccessKey, serializationCodec);
            redissonOpService.zRem(unameKey, tokenValue, serializationCodec);
            redissonOpService.zRem(clientId, tokenValue, serializationCodec);
            redissonOpService.del(TokenStoreConstants.ACCESS + key, serializationCodec);
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        String refreshKey = TokenStoreConstants.REFRESH + refreshToken.getValue();
        String refreshAuthKey = TokenStoreConstants.REFRESH_AUTH + refreshToken.getValue();
        redissonOpService.set(refreshKey, refreshToken, serializationCodec);
        redissonOpService.set(refreshAuthKey, authentication, serializationCodec);
        if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
            Date expiration = expiringRefreshToken.getExpiration();
            if (expiration != null) {
                int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
                redissonOpService.expire(refreshKey, seconds, serializationCodec);
                redissonOpService.expire(refreshAuthKey, seconds, serializationCodec);
            }
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        return redissonOpService.get(TokenStoreConstants.REFRESH + tokenValue, serializationCodec);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        removeRefreshToken(refreshToken.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        String refreshKey = TokenStoreConstants.REFRESH + tokenValue;
        String refreshAuthKey = TokenStoreConstants.REFRESH_AUTH + tokenValue;
        String refresh2AccessKey = TokenStoreConstants.REFRESH_TO_ACCESS + tokenValue;
        redissonOpService.del(refreshKey, serializationCodec);
        redissonOpService.del(refreshAuthKey, serializationCodec);
        String accessTokenValue = redissonOpService.get(refresh2AccessKey, serializationCodec);
        redissonOpService.del(refresh2AccessKey, serializationCodec);
        if (StringUtils.isNotBlank(accessTokenValue)) {
            redissonOpService.del(TokenStoreConstants.ACCESS_TO_REFRESH + accessTokenValue, serializationCodec);
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        String key = TokenStoreConstants.REFRESH_TO_ACCESS + refreshToken;
        String accessToken = redissonOpService.get(key, serializationCodec);
        redissonOpService.del(key, serializationCodec);
        if (StringUtils.isNotBlank(accessToken)) {
            removeAccessToken(accessToken);
        }
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        String key;
        if (StringUtils.isBlank(userName)) {
            key = TokenStoreConstants.CLIENT_ID_TO_ACCESS + clientId;
        } else {
            //approvalKey
            key = TokenStoreConstants.UNAME_TO_ACCESS + getApprovalKey(clientId, userName);
        }
        List<OAuth2AccessToken> oAuth2AccessTokenList = redissonOpService.lGet(key, serializationCodec);
        if (CollectionUtils.isEmpty(oAuth2AccessTokenList)) {
            return Collections.emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<>(oAuth2AccessTokenList.size());
        for (OAuth2AccessToken accessToken : oAuth2AccessTokenList) {
            accessTokens.add(accessToken);
        }
        return Collections.unmodifiableCollection(accessTokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return findTokensByClientIdAndUserName(clientId, "");
    }

}
