package com.zerosx.common.security.store;

import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.base.constants.TokenStoreConstants;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import com.zerosx.common.security.utils.JdkSerializationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 自定义TokenStore 与RedisTokenStore源码基本一致，不同：
 * 1）CLIENT_ID_TO_ACCESS、UNAME_TO_ACCESS改list结构存储
 * 2）增加自定义配置，如key前缀等等
 * 3）增加token自动续签逻辑
 */
@Slf4j
public class CustomRedisTokenStore implements TokenStore {

    private static final boolean springDataRedis_2_0 = ClassUtils.isPresent("org.springframework.data.redis.connection.RedisStandaloneConfiguration", RedisTokenStore.class.getClassLoader());

    private final RedisConnectionFactory connectionFactory;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    private String prefix = "";
    private Method redisConnectionSet_2_0;

    private RedissonOpService redissonOpService;
    private CustomSecurityProperties customSecurityProperties;


    public CustomRedisTokenStore(RedissonOpService redissonOpService, RedisConnectionFactory connectionFactory, CustomSecurityProperties customSecurityProperties) {
        this.redissonOpService = redissonOpService;
        this.connectionFactory = connectionFactory;
        this.customSecurityProperties = customSecurityProperties;
        this.setPrefix(customSecurityProperties.getTokenStorePrefix());
        if (springDataRedis_2_0) {
            this.loadRedisConnectionMethods_2_0();
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void loadRedisConnectionMethods_2_0() {
        this.redisConnectionSet_2_0 = ReflectionUtils.findMethod(RedisConnection.class, "set", byte[].class, byte[].class);
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    private byte[] serializeKey(String object) {
        return JdkSerializationUtils.serialize(prefix + object);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String key = authenticationKeyGenerator.extractKey(authentication);
        byte[] serializedKey = serializeKey(TokenStoreConstants.AUTH_TO_ACCESS + key);
        byte[] bytes;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        OAuth2AccessToken accessToken = JdkSerializationUtils.deserialize(bytes, OAuth2AccessToken.class);
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
                log.debug("客户端【{}】Token过期时间剩余20%，执行Token续签逻辑", oAuth2Authentication.getName());
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
        RedisConnection conn = getConnection();
        try {
            byte[] bytes = conn.get(serializeKey(TokenStoreConstants.AUTH + token));
            return JdkSerializationUtils.deserialize(bytes, OAuth2Authentication.class);
        } finally {
            conn.close();
        }
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        RedisConnection conn = getConnection();
        try {
            byte[] bytes = conn.get(serializeKey(TokenStoreConstants.REFRESH_AUTH + token));
            return JdkSerializationUtils.deserialize(bytes, OAuth2Authentication.class);
        } finally {
            conn.close();
        }
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        storeAccessToken(token, authentication, false);
    }


    public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication authentication, boolean renewToken) {
        byte[] serializedAccessToken = JdkSerializationUtils.serialize(oAuth2AccessToken);
        byte[] serializedAuth = JdkSerializationUtils.serialize(authentication);
        byte[] accessKey = serializeKey(TokenStoreConstants.ACCESS + oAuth2AccessToken.getValue());
        byte[] authKey = serializeKey(TokenStoreConstants.AUTH + oAuth2AccessToken.getValue());
        byte[] authToAccessKey = serializeKey(TokenStoreConstants.AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
        byte[] approvalKey = serializeKey(TokenStoreConstants.UNAME_TO_ACCESS + getApprovalKey(authentication));
        byte[] clientId = serializeKey(TokenStoreConstants.CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());

        RedisConnection conn = getConnection();
        try {
            byte[] previousAccessToken = conn.get(accessKey);
            // 如果token已存在，并且不是续签的话直接返回
            if (!renewToken && previousAccessToken != null) {
                log.debug("客户端【{}】token已存在，且不是续签token", authentication.getName());
                return;
            }
            conn.openPipeline();
            if (springDataRedis_2_0) {
                try {
                    this.redisConnectionSet_2_0.invoke(conn, accessKey, serializedAccessToken);
                    this.redisConnectionSet_2_0.invoke(conn, authKey, serializedAuth);
                    this.redisConnectionSet_2_0.invoke(conn, authToAccessKey, serializedAccessToken);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                conn.set(accessKey, serializedAccessToken);
                conn.set(authKey, serializedAuth);
                conn.set(authToAccessKey, serializedAccessToken);
            }
            // 如果是续签token，需要先删除集合里旧的值
            if (previousAccessToken != null) {
                if (!authentication.isClientOnly()) {
                    conn.lRem(approvalKey, 1, previousAccessToken);
                }
                conn.lRem(clientId, 1, previousAccessToken);
            }
            if (!authentication.isClientOnly()) {
                conn.rPush(approvalKey, serializedAccessToken);
            }
            conn.rPush(clientId, serializedAccessToken);
            if (oAuth2AccessToken.getExpiration() != null) {
                int seconds = oAuth2AccessToken.getExpiresIn();
                conn.expire(accessKey, seconds);
                conn.expire(authKey, seconds);
                conn.expire(authToAccessKey, seconds);
                conn.expire(clientId, seconds);
                conn.expire(approvalKey, seconds);
            }
            OAuth2RefreshToken refreshToken = oAuth2AccessToken.getRefreshToken();
            if (refreshToken != null && refreshToken.getValue() != null) {
                byte[] refresh = JdkSerializationUtils.serialize(refreshToken.getValue());
                byte[] access = JdkSerializationUtils.serialize(oAuth2AccessToken.getValue());
                byte[] refreshToAccessKey = serializeKey(TokenStoreConstants.REFRESH_TO_ACCESS + refreshToken.getValue());
                byte[] accessToRefreshKey = serializeKey(TokenStoreConstants.ACCESS_TO_REFRESH + oAuth2AccessToken.getValue());
                if (springDataRedis_2_0) {
                    try {
                        this.redisConnectionSet_2_0.invoke(conn, refreshToAccessKey, access);
                        this.redisConnectionSet_2_0.invoke(conn, accessToRefreshKey, refresh);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    conn.set(refreshToAccessKey, access);
                    conn.set(accessToRefreshKey, refresh);
                }
                if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                    ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                    Date expiration = expiringRefreshToken.getExpiration();
                    if (expiration != null) {
                        int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
                        conn.expire(refreshToAccessKey, seconds);
                        conn.expire(accessToRefreshKey, seconds);
                    }
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
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
        RedisConnection conn = getConnection();
        try {
            byte[] bytes = conn.get(serializeKey(TokenStoreConstants.ACCESS + tokenValue));
            return JdkSerializationUtils.deserialize(bytes, OAuth2AccessToken.class);
        } finally {
            conn.close();
        }
    }

    public void removeAccessToken(String tokenValue) {
        byte[] accessKey = serializeKey(TokenStoreConstants.ACCESS + tokenValue);
        byte[] authKey = serializeKey(TokenStoreConstants.AUTH + tokenValue);
        byte[] accessToRefreshKey = serializeKey(TokenStoreConstants.ACCESS_TO_REFRESH + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(accessKey);
            conn.get(authKey);
            conn.del(accessKey);
            conn.del(accessToRefreshKey);
            // Don't remove the refresh token - it's up to the caller to do that
            conn.del(authKey);
            List<Object> results = conn.closePipeline();
            byte[] access = (byte[]) results.get(0);
            byte[] auth = (byte[]) results.get(1);

            OAuth2Authentication authentication = JdkSerializationUtils.deserialize(auth, OAuth2Authentication.class);
            if (authentication != null) {
                String key = authenticationKeyGenerator.extractKey(authentication);
                byte[] authToAccessKey = serializeKey(TokenStoreConstants.AUTH_TO_ACCESS + key);
                byte[] unameKey = serializeKey(TokenStoreConstants.UNAME_TO_ACCESS + getApprovalKey(authentication));
                byte[] clientId = serializeKey(TokenStoreConstants.CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
                conn.openPipeline();
                conn.del(authToAccessKey);
                conn.lRem(unameKey, 1, access);
                conn.lRem(clientId, 1, access);
                conn.del(serializeKey(TokenStoreConstants.ACCESS + key));
                conn.closePipeline();
            }
        } finally {
            conn.close();
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        byte[] refreshKey = serializeKey(TokenStoreConstants.REFRESH + refreshToken.getValue());
        byte[] refreshAuthKey = serializeKey(TokenStoreConstants.REFRESH_AUTH + refreshToken.getValue());
        byte[] serializedRefreshToken = JdkSerializationUtils.serialize(refreshToken);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            if (springDataRedis_2_0) {
                try {
                    this.redisConnectionSet_2_0.invoke(conn, refreshKey, serializedRefreshToken);
                    this.redisConnectionSet_2_0.invoke(conn, refreshAuthKey, JdkSerializationUtils.serialize(authentication));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                conn.set(refreshKey, serializedRefreshToken);
                conn.set(refreshAuthKey, JdkSerializationUtils.serialize(authentication));
            }
            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                Date expiration = expiringRefreshToken.getExpiration();
                if (expiration != null) {
                    int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
                    conn.expire(refreshKey, seconds);
                    conn.expire(refreshAuthKey, seconds);
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        RedisConnection conn = getConnection();
        try {
            byte[] bytes = conn.get(serializeKey(TokenStoreConstants.REFRESH + tokenValue));
            return JdkSerializationUtils.deserialize(bytes, OAuth2RefreshToken.class);
        } finally {
            conn.close();
        }
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        removeRefreshToken(refreshToken.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = serializeKey(TokenStoreConstants.REFRESH + tokenValue);
        byte[] refreshAuthKey = serializeKey(TokenStoreConstants.REFRESH_AUTH + tokenValue);
        byte[] refresh2AccessKey = serializeKey(TokenStoreConstants.REFRESH_TO_ACCESS + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.del(refreshKey);
            conn.del(refreshAuthKey);
            conn.get(refresh2AccessKey);
            conn.del(refresh2AccessKey);
            List<Object> results = conn.closePipeline();
            byte[] accessTokenBytes = (byte[]) results.get(2);
            if (accessTokenBytes != null) {
                String accessTokenValue = JdkSerializationUtils.deserialize(accessTokenBytes);
                conn.del(serializeKey(TokenStoreConstants.ACCESS_TO_REFRESH + accessTokenValue));
            }
        } finally {
            conn.close();
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        byte[] key = serializeKey(TokenStoreConstants.REFRESH_TO_ACCESS + refreshToken);
        List<Object> results;
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(key);
            results = conn.closePipeline();
        } finally {
            conn.close();
        }
        byte[] bytes = (byte[]) results.get(0);
        String accessToken = JdkSerializationUtils.deserialize(bytes);
        if (accessToken != null) {
            removeAccessToken(accessToken);
        }
    }

    private List<byte[]> getByteLists(byte[] approvalKey, RedisConnection conn) {
        List<byte[]> byteList;
        Long size = conn.sCard(approvalKey);
        byteList = new ArrayList<>(size.intValue());
        Cursor<byte[]> cursor = conn.sScan(approvalKey, ScanOptions.NONE);
        while (cursor.hasNext()) {
            byteList.add(cursor.next());
        }
        return byteList;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        byte[] approvalKey = serializeKey(TokenStoreConstants.UNAME_TO_ACCESS + getApprovalKey(clientId, userName));
        List<byte[]> byteList;
        RedisConnection conn = getConnection();
        try {
            byteList = getByteLists(approvalKey, conn);
        } finally {
            conn.close();
        }
        if (byteList.isEmpty()) {
            return Collections.emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<>(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = JdkSerializationUtils.deserialize(bytes, OAuth2AccessToken.class);
            accessTokens.add(accessToken);
        }
        return Collections.unmodifiableCollection(accessTokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        byte[] key = serializeKey(TokenStoreConstants.CLIENT_ID_TO_ACCESS + clientId);
        List<byte[]> byteList;
        RedisConnection conn = getConnection();
        try {
            byteList = getByteLists(key, conn);
        } finally {
            conn.close();
        }
        if (byteList.size() == 0) {
            return Collections.emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<>(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = JdkSerializationUtils.deserialize(bytes, OAuth2AccessToken.class);
            accessTokens.add(accessToken);
        }
        return Collections.unmodifiableCollection(accessTokens);
    }


}
