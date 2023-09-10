package com.zerosx.common.oss.core.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AliyunOssConfig extends BaseOssConfig{

    /**
     * 访问端点
     */
    private String endpoint;

    /**
     * 阿里云限制有效时间是60-32400，单位：秒
     */
    private Long expireTime = 28800L;

}
