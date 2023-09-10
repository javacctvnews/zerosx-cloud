package com.zerosx.sms.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * DefaultSmsProperties
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-10 17:05
 **/
@Setter
@Getter
public class DefaultSmsProperties {

    /**
     * sms服务商编码
     */
    private String supplierType;

    /**
     * Access Key
     */
    private String accessKeyId;

    /**
     * Access Key Secret
     */
    private String accessKeySecret;

    /**
     * 地域信息默认为 cn-hangzhou
     */
    private String regionId;

    /**
     * 短信签名
     */
    private String signature;
    /**
     * 聚合短信 key
     */
    private String key;

    /**
     * 聚合短信 api地址
     * http://v.juhe.cn/sms/send
     */
    private String domainAddress;

}
