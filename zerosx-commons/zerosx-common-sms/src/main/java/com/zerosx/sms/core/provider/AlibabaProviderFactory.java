package com.zerosx.sms.core.provider;

import com.zerosx.sms.core.client.AlibabaSmsClient;
import com.zerosx.sms.core.config.AlibabaConfig;
import com.zerosx.sms.enums.SupplierTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AlibabaProviderFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:36
 **/
public class AlibabaProviderFactory implements ISmsProviderFactory<AlibabaSmsClient, AlibabaConfig> {

    private static final Logger log = LoggerFactory.getLogger(AlibabaProviderFactory.class);

    /**
     * key 是 accessKey + regionId
     */
    private static final Map<String, AlibabaSmsClient> clientMap = new ConcurrentHashMap<>();

    private static final AlibabaProviderFactory INSTANCE = new AlibabaProviderFactory();

    private AlibabaProviderFactory() {

    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static AlibabaProviderFactory instance() {
        return INSTANCE;
    }

    /**
     * 缓存实例的key
     *
     * @param config
     * @return
     */
    private String key(AlibabaConfig config) {
        return config.getAccessKeyId() + config.getRegionId();
    }

    @Override
    public AlibabaSmsClient createSms(AlibabaConfig config) {
        String key = key(config);
        AlibabaSmsClient client = clientMap.get(key);
        if (client == null) {
            return createMultiSms(config);
        }
        return client;
    }

    @Override
    public AlibabaSmsClient createMultiSms(AlibabaConfig config) {
        AlibabaSmsClient client = new AlibabaSmsClient(config);
        clientMap.put(key(config), client);
        log.debug("创建【{}】实例, 当前{}个AlibabaSmsClient", SupplierTypeEnum.ALIBABA.getCode(), clientMap.size());
        return client;
    }

    @Override
    public AlibabaSmsClient refresh(AlibabaConfig config) {
        return createMultiSms(config);
    }

    @Override
    public void removeClient(String operatorId) {
        clientMap.remove(operatorId);
    }
}
