package com.zerosx.sms.enums;


import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import com.zerosx.sms.core.client.IMultiSmsClient;
import com.zerosx.sms.core.config.AlibabaConfig;
import com.zerosx.sms.core.config.ISupplierConfig;
import com.zerosx.sms.core.config.JdCloudConfig;
import com.zerosx.sms.core.config.JuheConfig;
import com.zerosx.sms.core.provider.AlibabaProviderFactory;
import com.zerosx.sms.core.provider.ISmsProviderFactory;
import com.zerosx.sms.core.provider.JdCloudProviderFactory;
import com.zerosx.sms.core.provider.JuheProviderFactory;
import lombok.Getter;

/**
 * SupplierType
 * <p> sms供应商
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:03
 **/
@Getter
@AutoDictData(name = "短信服务商")
public enum SupplierTypeEnum implements BaseEnum<String> {

    /**
     * 阿里云
     */
    ALIBABA("alibaba", "阿里云短信", AlibabaProviderFactory.instance(), AlibabaConfig.class),
    /**
     * 聚合
     * 官网：<a href="https://www.juhe.cn/docs/api/id/54">聚合短信</a>
     */
    JUHE("juhe", "聚合短信", JuheProviderFactory.instance(), JuheConfig.class),
    /**
     * 京东云
     */
    JDCLOUD("jdcloud", "京东云短信", JdCloudProviderFactory.instance(), JdCloudConfig.class);

    /**
     * 渠道编码
     */
    private final String code;
    /**
     * 渠道名称
     */
    private final String message;

    /**
     * 建造者工厂
     */
    private final ISmsProviderFactory<? extends IMultiSmsClient, ? extends ISupplierConfig> providerFactory;
    /**
     * 配置类
     */
    private final Class<? extends ISupplierConfig> configClass;

    SupplierTypeEnum(String code, String message, ISmsProviderFactory<? extends IMultiSmsClient, ? extends ISupplierConfig> providerFactory, Class<? extends ISupplierConfig> configClass) {
        this.code = code;
        this.message = message;
        this.providerFactory = providerFactory;
        this.configClass = configClass;
    }

    /**
     * 通过编码获取
     *
     * @param code
     * @return
     */
    public static SupplierTypeEnum getSupplierType(String code) {
        SupplierTypeEnum[] values = SupplierTypeEnum.values();
        for (SupplierTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new RuntimeException("非法的SMS服务商：" + code);
    }

    /**
     * 通过编码获取名称
     *
     * @param code
     * @return
     */
    public static String getSupplierName(String code) {
        SupplierTypeEnum[] values = SupplierTypeEnum.values();
        for (SupplierTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getMessage();
            }
        }
        return "";
    }

}
