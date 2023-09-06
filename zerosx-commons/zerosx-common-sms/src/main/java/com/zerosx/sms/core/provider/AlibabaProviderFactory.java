package com.zerosx.sms.core.provider;

import com.zerosx.sms.core.client.AlibabaSmsClient;
import com.zerosx.sms.core.config.AlibabaConfig;
import com.zerosx.sms.enums.SupplierTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AlibabaProviderFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:36
 **/
@Slf4j
public class AlibabaProviderFactory implements ISmsProviderFactory<AlibabaSmsClient, AlibabaConfig> {

    private static final Map<String, AlibabaSmsClient> clientMap = new ConcurrentHashMap<>();

    private static final AlibabaProviderFactory INSTANCE = new AlibabaProviderFactory();

    private AlibabaProviderFactory() {
    }

    private static final class ConfigHolder {
        private static AlibabaConfig config = AlibabaConfig.builder().build();
    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static AlibabaProviderFactory instance() {
        return INSTANCE;
    }

    @Override
    public AlibabaSmsClient createSms(AlibabaConfig config) {
        AlibabaSmsClient client = clientMap.get(config.getOperatorId());
        if (client == null) {
            return createMultiSms(config);
        }
        return client;
    }

    @Override
    public AlibabaSmsClient createMultiSms(AlibabaConfig config) {
        AlibabaSmsClient client = new AlibabaSmsClient(config);
        clientMap.put(config.getOperatorId(), client);
        log.debug("[{}]创建【{}】实例, 当前{}个AlibabaSmsClient", config.getOperatorId(), SupplierTypeEnum.ALIBABA.getCode(), clientMap.size());
        return client;
    }

    @Override
    public AlibabaSmsClient refresh(AlibabaConfig config) {
        return createMultiSms(config);
    }

    @Override
    public AlibabaConfig getConfig() {
        return ConfigHolder.config;
    }

    @Override
    public void setConfig(AlibabaConfig config) {
        ConfigHolder.config = config;
    }

    @Override
    public void removeClient(String operatorId) {
        clientMap.remove(operatorId);
    }
}
