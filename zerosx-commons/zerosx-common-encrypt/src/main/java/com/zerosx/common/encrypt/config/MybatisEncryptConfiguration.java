package com.zerosx.common.encrypt.config;

import com.zerosx.common.encrypt.core.EncryptorCacheManager;
import com.zerosx.common.encrypt.core.ICustomEncryptor;
import com.zerosx.common.encrypt.interceptor.DecryptInterceptor;
import com.zerosx.common.encrypt.interceptor.EncryptInterceptor;
import com.zerosx.common.encrypt.properties.CustomEncryptProperties;
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

    @Bean
    public ICustomEncryptor customEncryptor(EncryptorCacheManager encryptorCacheManager) {
        return encryptorCacheManager.getEncryptor();
    }

    @Bean
    public EncryptInterceptor encryptInterceptor(EncryptorCacheManager encryptorCacheManager) {
        return new EncryptInterceptor(encryptorCacheManager, customEncryptProperties);
    }

    @Bean
    public DecryptInterceptor decryptInterceptor(EncryptorCacheManager encryptorCacheManager) {
        return new DecryptInterceptor(encryptorCacheManager, customEncryptProperties);
    }

}
