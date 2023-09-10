package com.zerosx.common.oss.core.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QiniuOssConfig extends BaseOssConfig{

    /**
     * 域名
     */
    private String domainAddress;

    /**
     * 七牛云无限制，默认8小时，单位：秒
     */
    private Long expireTime = 28800L;
}
