package com.zerosx.sms.core.provider;

import com.zerosx.sms.core.client.JuheSmsClient;
import com.zerosx.sms.core.config.JuheConfig;
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
public class JuheProviderFactory implements ISmsProviderFactory<JuheSmsClient, JuheConfig> {

    private static final Map<String, JuheSmsClient> clientMap = new ConcurrentHashMap<>();

    private static final JuheProviderFactory INSTANCE = new JuheProviderFactory();

    private JuheProviderFactory() {
    }

    private static final class ConfigHolder {
        private static JuheConfig config = new JuheConfig();
    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static JuheProviderFactory instance() {
        return INSTANCE;
    }

    @Override
    public JuheSmsClient createSms(JuheConfig config) {
        JuheSmsClient client = clientMap.get(config.getKey());
        if (client == null) {
            return createMultiSms(config);
        }
        return client;
    }

    @Override
    public JuheSmsClient createMultiSms(JuheConfig config) {
        JuheSmsClient client = new JuheSmsClient(config);
        clientMap.put(config.getKey(), client);
        log.debug("[{}]创建【{}】实例, 当前{}个JuheSmsClient", config.getOperatorId(), SupplierTypeEnum.JUHE.getCode(), clientMap.size());
        return client;
    }

    @Override
    public JuheSmsClient refresh(JuheConfig config) {
        return createMultiSms(config);
    }

    @Override
    public JuheConfig getConfig() {
        return JuheProviderFactory.ConfigHolder.config;
    }

    @Override
    public void setConfig(JuheConfig config) {
        JuheProviderFactory.ConfigHolder.config = config;
    }

    @Override
    public void removeClient(String operatorId) {
        clientMap.remove(operatorId);
    }

}
