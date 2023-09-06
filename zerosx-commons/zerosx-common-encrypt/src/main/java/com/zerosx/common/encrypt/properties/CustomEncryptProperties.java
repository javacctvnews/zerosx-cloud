package com.zerosx.common.encrypt.properties;

import com.zerosx.common.encrypt.enums.AlgorithmEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zerosx.mybatis-crypto")
public class CustomEncryptProperties {

    /**
     * 是否开启mybatis敏感数据加解密，默认开启
     */
    private Boolean enabled = true;

    /**
     * 加密算法 默认aes
     */
    private String algorithm = AlgorithmEnum.SM4.getCode();

    /**
     * 加密秘钥
     */
    private String key = "1234567898765432";

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
