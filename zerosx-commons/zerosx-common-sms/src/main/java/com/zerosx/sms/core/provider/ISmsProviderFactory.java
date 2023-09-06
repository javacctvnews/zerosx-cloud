package com.zerosx.sms.core.provider;

import com.zerosx.sms.core.client.IMultiSmsClient;
import com.zerosx.sms.core.config.ISupplierConfig;

/**
 * ISmsProviderFactory
 * <p>短信对象建造者
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:34
 **/
public interface ISmsProviderFactory<T extends IMultiSmsClient, R extends ISupplierConfig> {

    /**
     * 创建短信实现对象
     *
     * @param c 短信配置对象
     * @return 短信实现对象
     */
    T createSms(R c);

    /**
     * 创建短信实现对象
     *
     * @param c 短信配置对象
     * @return 短信实现对象
     */
    T createMultiSms(R c);

    /**
     * 刷新短信实现对象
     * @param c 短信配置对象
     * @return 刷新后的短信实现对象
     */
    T refresh(R c);

    /**
     * 设置配置
     *
     * @param config 配置对象
     */
    void setConfig(R config);

    /**
     * 获取配置
     *
     * @return
     */
    R getConfig();

    /**
     * 移除client
     * @param operatorId
     */
    void removeClient(String operatorId);
}
