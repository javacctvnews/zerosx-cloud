package com.zerosx.common.oss.core.provider;

import com.zerosx.common.oss.core.client.AliyunClientService;
import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.core.config.AliyunOssConfig;
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
public class AliyunOssProviderFactory implements IOssProviderFactory<AliyunOssConfig> {

    private static final Map<String, IOssClientService> OSS_CLIENT_MAP = new ConcurrentHashMap<>();

    private static final AliyunOssProviderFactory INSTANCE = new AliyunOssProviderFactory();

    private AliyunOssProviderFactory() {

    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static AliyunOssProviderFactory instance() {
        return INSTANCE;
    }

    private String getKey(AliyunOssConfig ossConfig) {
        return ossConfig.getEndpoint() + ossConfig.getAccessKeyId();
    }

    @Override
    public IOssClientService createClient(AliyunOssConfig ossConfig) {
        if (ossConfig.getOssSupplierId() == null) {
            return refreshClient(ossConfig);
        }
        String mapKey = getKey(ossConfig);
        IOssClientService ossClientService = OSS_CLIENT_MAP.get(mapKey);
        if (ossClientService == null) {
            ossClientService = refreshClient(ossConfig);
            OSS_CLIENT_MAP.put(mapKey, ossClientService);
            log.debug("创建【{}】实例，AccessKey:{}", OssTypeEnum.ALIBABA.getCode(), ossConfig.getAccessKeyId());
        }
        return ossClientService;
    }

    @Override
    public IOssClientService refreshClient(AliyunOssConfig ossConfig) {
        return new AliyunClientService(ossConfig);
    }

}
