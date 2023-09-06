package com.zerosx.sms.core;

import com.zerosx.sms.core.client.IMultiSmsClient;
import com.zerosx.sms.core.config.ISupplierConfig;
import com.zerosx.sms.core.provider.ISmsProviderFactory;
import com.zerosx.sms.enums.SupplierTypeEnum;

/**
 * SmsFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:03
 **/
public abstract class SmsFactory {

    private SmsFactory() {
    }

    /**
     * 获取各个厂商的实现类
     *
     * @param supplierTypeEnum
     * @return
     */
    public static IMultiSmsClient createSmsClient(SupplierTypeEnum supplierTypeEnum) {
        ISmsProviderFactory providerFactory = supplierTypeEnum.getProviderFactory();
        return providerFactory.createSms(providerFactory.getConfig());
    }

    /**
     * 获取各个厂商的多例实现对象
     *
     * @param supplierTypeEnum
     * @param config
     * @return
     */
    public static IMultiSmsClient createSmsClient(SupplierTypeEnum supplierTypeEnum, ISupplierConfig config) {
        return createSmsClient(supplierTypeEnum, config, false);
    }

    /**
     * 获取各个厂商的多例实现对象
     *
     * @param supplierTypeEnum
     * @param config
     * @param refresh      是否刷新实例
     * @return
     */
    public static IMultiSmsClient createSmsClient(SupplierTypeEnum supplierTypeEnum, ISupplierConfig config, boolean refresh) {
        ISmsProviderFactory providerFactory = supplierTypeEnum.getProviderFactory();
        return refresh ? providerFactory.createMultiSms(config) : providerFactory.createSms(config);
    }

    /**
     * 刷新sms实例
     *
     * @param supplierTypeEnum
     */
    public static void refreshClient(SupplierTypeEnum supplierTypeEnum) {
        ISmsProviderFactory providerFactory = supplierTypeEnum.getProviderFactory();
        providerFactory.refresh(providerFactory.getConfig());
    }

    /**
     * 刷新sms实例
     *
     * @param supplierTypeEnum
     */
    public static void refreshClient(SupplierTypeEnum supplierTypeEnum, ISupplierConfig config) {
        ISmsProviderFactory providerFactory = supplierTypeEnum.getProviderFactory();
        providerFactory.refresh(config);
    }

    public static void removeClient(SupplierTypeEnum supplierTypeEnum, String operatorId) {
        ISmsProviderFactory providerFactory = supplierTypeEnum.getProviderFactory();
        providerFactory.removeClient(operatorId);
    }
}
