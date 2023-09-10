package com.zerosx.common.oss.core.config;

import lombok.Data;

/**
 * TencentProperties
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-08 14:32
 **/
@Data
public class TencentOssConfig extends BaseOssConfig {

    /**
     * 腾讯云无限制，默认8小时，单位：秒
     */
    private Long expireTime = 28800L;

}
