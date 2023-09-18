package com.zerosx.encrypt2.core.properties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * EncryptProperties
 * <p> 加解密配置，需要外部注入bean
 *
 * @author: javacctvnews
 * @create: 2023-09-13 10:25
 **/
@Data
@Configuration
public class EncryptProperties {

    /**
     * 是否启用，默认启用
     */
    private Boolean enabled = true;

    /**
     * 加密密码
     */
    private String password;


    /**
     * 是否开启debug模式
     */
    private Boolean debug = false;

}
