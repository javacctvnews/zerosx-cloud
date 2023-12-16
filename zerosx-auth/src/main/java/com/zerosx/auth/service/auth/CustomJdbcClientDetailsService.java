package com.zerosx.auth.service.auth;

import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.redis.templete.RedissonOpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * 对oauth_client_details表数据进行redis缓存，提高性能
 */
@Service
@Slf4j
public class CustomJdbcClientDetailsService extends JdbcClientDetailsService {

    @Autowired
    private RedissonOpService redissonOpService;

    public CustomJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        //return super.loadClientByClientId(clientId);
        String cacheKey = ZCache.OAUTH_CLIENT_DETAILS.key(clientId);
        ClientDetails object = redissonOpService.get(cacheKey);
        if (object != null) {
            return object;
        }
        ClientDetails clientDetails = super.loadClientByClientId(clientId);
        redissonOpService.set(cacheKey, clientDetails);
        log.debug("更新客户端【{}】缓存", clientId);
        return clientDetails;
    }

}
