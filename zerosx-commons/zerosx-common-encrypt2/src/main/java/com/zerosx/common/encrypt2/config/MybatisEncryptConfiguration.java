package com.zerosx.common.encrypt2.config;

import com.zerosx.common.encrypt2.core.EncryptorCacheManager;
import com.zerosx.common.encrypt2.interceptor.DecryptInterceptor;
import com.zerosx.common.encrypt2.interceptor.EncryptInterceptor22;
import com.zerosx.common.encrypt2.properties.CustomEncryptProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({CustomEncryptProperties.class})
@ConditionalOnProperty(value = "zerosx.mybatis-crypto.enabled", havingValue = "true")
public class MybatisEncryptConfiguration {

    @Autowired
    private CustomEncryptProperties customEncryptProperties;

    @Bean
    public EncryptorCacheManager encryptorCacheManager() {
        return new EncryptorCacheManager(customEncryptProperties);
    }

    /*@Bean
    public ICustomEncryptor customEncryptor() {
        return EncryptorCacheManager.getEncryptor(customEncryptProperties);
    }*/

    /*@Bean
    public EncryptInterceptor encryptInterceptor(EncryptorCacheManager encryptorCacheManager) {
        return new EncryptInterceptor(encryptorCacheManager, customEncryptProperties);
    }*/

    @Bean
    public EncryptInterceptor22 encryptInterceptor(EncryptorCacheManager encryptorCacheManager) {
        return new EncryptInterceptor22(encryptorCacheManager);
    }

    @Bean
    public DecryptInterceptor decryptInterceptor(EncryptorCacheManager encryptorCacheManager) {
        return new DecryptInterceptor(encryptorCacheManager, customEncryptProperties);
    }

}
