package com.zerosx.common.oss.core.provider;

import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.core.client.TencentClientService;
import com.zerosx.common.oss.core.config.TencentOssConfig;
import com.zerosx.common.oss.enums.OssTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AliyunOssProviderFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-08 16:17
 **/
@Slf4j
public class TencentOssProviderFactory implements IOssProviderFactory<TencentOssConfig> {

    private static final Map<String, IOssClientService> OSS_CLIENT_MAP = new ConcurrentHashMap<>();

    private static final TencentOssProviderFactory INSTANCE = new TencentOssProviderFactory();

    private TencentOssProviderFactory() {

    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static TencentOssProviderFactory instance() {
        return INSTANCE;
    }

    private String getKey(TencentOssConfig ossConfig) {
        return ossConfig.getRegionId() + ossConfig.getAccessKeyId();
    }

    @Override
    public IOssClientService createClient(TencentOssConfig ossConfig) {
        if (ossConfig.getOssSupplierId() == null) {
            return refreshClient(ossConfig);
        }
        String key = getKey(ossConfig);
        IOssClientService ossClientService = OSS_CLIENT_MAP.get(key);
        if (ossClientService == null) {
            ossClientService = refreshClient(ossConfig);
            OSS_CLIENT_MAP.put(ossConfig.getAccessKeyId(), ossClientService);
            log.debug("创建【{}】实例，AccessKey:{}", OssTypeEnum.TENCENT.getCode(), ossConfig.getAccessKeyId());
        }
        return ossClientService;
    }

    @Override
    public IOssClientService refreshClient(TencentOssConfig ossConfig) {
        return new TencentClientService(ossConfig);
    }

}
