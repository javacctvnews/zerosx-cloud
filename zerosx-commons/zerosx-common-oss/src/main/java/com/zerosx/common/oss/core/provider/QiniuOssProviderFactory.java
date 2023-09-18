package com.zerosx.common.oss.core.provider;

import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.core.client.QiniuClientService;
import com.zerosx.common.oss.core.config.QiniuOssConfig;
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
public class QiniuOssProviderFactory implements IOssProviderFactory<QiniuOssConfig> {

    private static final Map<String, IOssClientService> OSS_CLIENT_MAP = new ConcurrentHashMap<>();

    private static final QiniuOssProviderFactory INSTANCE = new QiniuOssProviderFactory();

    private QiniuOssProviderFactory() {
    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static QiniuOssProviderFactory instance() {
        return INSTANCE;
    }

    private String getKey(QiniuOssConfig ossConfig) {
        return ossConfig.getAccessKeyId();
    }

    @Override
    public IOssClientService createClient(QiniuOssConfig ossConfig) {
        if (ossConfig.getOssSupplierId() == null) {
            return refreshClient(ossConfig);
        }
        String key = getKey(ossConfig);
        IOssClientService ossClientService = OSS_CLIENT_MAP.get(key);
        if (ossClientService == null) {
            ossClientService = refreshClient(ossConfig);
            OSS_CLIENT_MAP.put(key, ossClientService);
            log.debug("创建【{}】实例，AccessKey:{}", OssTypeEnum.QINIU.getCode(), ossConfig.getAccessKeyId());
        }
        return ossClientService;
    }

    @Override
    public IOssClientService refreshClient(QiniuOssConfig ossConfig) {
        return new QiniuClientService(ossConfig);
    }

}
