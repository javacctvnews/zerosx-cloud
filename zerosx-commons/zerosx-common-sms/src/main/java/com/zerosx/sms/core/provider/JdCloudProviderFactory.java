package com.zerosx.sms.core.provider;

import com.zerosx.sms.core.client.JdCloudSmsClient;
import com.zerosx.sms.core.config.JdCloudConfig;
import com.zerosx.sms.enums.SupplierTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JuheProviderFactory
 * <p> 聚合短信
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:53
 **/
@Slf4j
public class JdCloudProviderFactory implements ISmsProviderFactory<JdCloudSmsClient, JdCloudConfig> {

    private static final Map<String, JdCloudSmsClient> clientMap = new ConcurrentHashMap<>();

    private static final JdCloudProviderFactory INSTANCE = new JdCloudProviderFactory();

    private JdCloudProviderFactory() {
    }

    private static final class ConfigHolder {
        private static JdCloudConfig config = new JdCloudConfig();
    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static JdCloudProviderFactory instance() {
        return INSTANCE;
    }


    private String key(JdCloudConfig config) {
        return config.getAccessKeyId() + config.getRegionId();
    }

    @Override
    public JdCloudSmsClient createSms(JdCloudConfig config) {
        JdCloudSmsClient client = clientMap.get(key(config));
        if (client == null) {
            return createMultiSms(config);
        }
        return client;
    }

    @Override
    public JdCloudSmsClient createMultiSms(JdCloudConfig config) {
        JdCloudSmsClient client = new JdCloudSmsClient(config);
        clientMap.put(key(config), client);
        log.debug("[{}]创建【{}】实例, 当前{}个JdCloudSmsClient", config.getOperatorId(), SupplierTypeEnum.JUHE.getCode(), clientMap.size());
        return client;
    }

    @Override
    public JdCloudSmsClient refresh(JdCloudConfig config) {
        return createMultiSms(config);
    }

    @Override
    public JdCloudConfig getConfig() {
        return JdCloudProviderFactory.ConfigHolder.config;
    }

    @Override
    public void setConfig(JdCloudConfig config) {
        JdCloudProviderFactory.ConfigHolder.config = config;
    }

    @Override
    public void removeClient(String operatorId) {
        clientMap.remove(operatorId);
    }

}
