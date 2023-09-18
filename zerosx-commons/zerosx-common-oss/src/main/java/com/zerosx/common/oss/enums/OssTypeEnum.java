package com.zerosx.common.oss.enums;


import com.zerosx.common.oss.core.config.AliyunOssConfig;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.core.config.QiniuOssConfig;
import com.zerosx.common.oss.core.config.TencentOssConfig;
import com.zerosx.common.oss.core.provider.AliyunOssProviderFactory;
import com.zerosx.common.oss.core.provider.IOssProviderFactory;
import com.zerosx.common.oss.core.provider.QiniuOssProviderFactory;
import com.zerosx.common.oss.core.provider.TencentOssProviderFactory;
import lombok.Getter;

/**
 * OssTypeEnum
 * <p> OSS服务商
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:03
 **/
@Getter
public enum OssTypeEnum {

    /**
     * 阿里云
     */
    ALIBABA("alibaba", "阿里云", AliyunOssProviderFactory.instance(), AliyunOssConfig.class),

    /**
     * 腾讯云
     */
    TENCENT("tencent", "腾讯云", TencentOssProviderFactory.instance(), TencentOssConfig.class),

    /**
     * 七牛云
     */
    QINIU("qiniu", "七牛云", QiniuOssProviderFactory.instance(), QiniuOssConfig.class),
    ;

    /**
     * 渠道编码
     */
    private final String code;
    /**
     * 渠道名称
     */
    private final String message;

    /**
     * oss实例建造者工厂
     */
    private final IOssProviderFactory providerFactory;
    /**
     * oss配置class，用于创建实例时加载哪个配置
     */
    private final Class<? extends IOssConfig> configClz;

    OssTypeEnum(String code, String message, IOssProviderFactory providerFactory, Class<? extends IOssConfig> configClz) {
        this.code = code;
        this.message = message;
        this.providerFactory = providerFactory;
        this.configClz = configClz;
    }

    public static OssTypeEnum getOssType(String code) {
        OssTypeEnum[] values = OssTypeEnum.values();
        for (OssTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new RuntimeException("非法的SMS服务商：" + code);
    }

    public static String getOssName(String code) {
        OssTypeEnum[] values = OssTypeEnum.values();
        for (OssTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getMessage();
            }
        }
        return "";
    }
}
