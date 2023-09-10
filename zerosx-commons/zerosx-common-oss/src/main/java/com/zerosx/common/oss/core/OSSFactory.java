package com.zerosx.common.oss.core;

import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.core.provider.TencentOssProviderFactory;
import com.zerosx.common.oss.enums.OssTypeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * OSSFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-08 13:00
 **/
@Slf4j
public abstract class OSSFactory {

    private OSSFactory() {
    }

    /**
     * 获取实例（不存在时重新创建）
     * @param ossType
     * @param ossConfig
     * @return
     */
    public static IOssClientService createClient(OssTypeEnum ossType, IOssConfig ossConfig) {
        return ossType.getProviderFactory().createClient(ossConfig);
    }

    /**
     * 刷新实例
     * @param ossType
     * @param ossConfig
     * @return
     */
    public static IOssClientService refreshClient(OssTypeEnum ossType, IOssConfig ossConfig) {
        if (ossConfig == null) {
            log.warn("刷新OSS【{}】实例，配置不能为空", ossType.getCode());
            return null;
        }
        return ossType.getProviderFactory().refreshClient(ossConfig);
    }

}
