package com.zerosx.common.encrypt2.properties;

import com.zerosx.common.encrypt2.enums.AlgorithmEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "zerosx.mybatis-crypto")
public class CustomEncryptProperties {

    /**
     * 是否开启mybatis敏感数据加解密，默认开启
     */
    private Boolean enabled = true;

    /**
     * 加密算法 默认aes
     */
    private String algorithm = AlgorithmEnum.AES.getCode();

    /**
     * 加密秘钥
     */
    private String key = "1234567898765432";


}
