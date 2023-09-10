package com.zerosx.sms.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * JdCloudConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-10 16:14
 **/
@Setter@Getter
public class JdCloudConfig extends BaseSupplierConfig{

    /**
     * 地域信息默认为 cn-hangzhou
     */
    private String regionId = "";

}
