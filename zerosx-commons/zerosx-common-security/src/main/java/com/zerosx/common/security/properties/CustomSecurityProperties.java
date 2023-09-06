package com.zerosx.common.security.properties;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.constants.SecurityConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 认证授权 相关配置
 */
@Data
@ConfigurationProperties(prefix = "zerosx.security")
public class CustomSecurityProperties {

    /**
     * token存储类型(redis/db/authJwt/resJwt)
     * 默认redis
     */
    private String tokenStoreType = "redis";
    /**
     * Token存储前缀
     */
    private String tokenStorePrefix = CommonConstants.ZEROSX;
    /**
     * jwt的signingKey
     */
    private String signingKey = CommonConstants.ZEROSX;
    /**
     * 是否续签token，默认token剩余有效时间小于过期时长的80%时自动续签
     */
    private Boolean renewToken = true;
    /**
     * 续签token, token剩余时间/有效时间 <= RENEW_TOKEN_PERCENT
     */
    private Double renewTokenPercent = 0.2;

    /**
     * 不需认证即可访问系统的URL，即不校验token
     */
    private List<String> ignoreAuthUrls = new ArrayList<>();

    private PermissionProperties perms;

    /**
     * 所有不需要认证的url
     *
     * @return
     */
    public List<String> getAllIgnoreAuthUrls() {
        List<String> allIgnoreAuthUrls = new ArrayList<>();
        allIgnoreAuthUrls.addAll(Arrays.asList(SecurityConstants.ENDPOINTS));
        allIgnoreAuthUrls.addAll(ignoreAuthUrls);
        return allIgnoreAuthUrls;
    }


}
