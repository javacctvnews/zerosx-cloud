package com.zerosx.common.encrypt.core.encryptor;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.zerosx.common.encrypt.core.ICustomEncryptor;
import com.zerosx.common.encrypt.enums.AlgorithmEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
public class AesEncryptor implements ICustomEncryptor {

    private static final String DEFAULT_KEY = "1234567898765432";

    private final AES aes;

    public AesEncryptor() {
        this.aes = SecureUtil.aes(DEFAULT_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public AesEncryptor(String password) {
        this.aes = SecureUtil.aes(password.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getAlgorithm() {
        return AlgorithmEnum.AES.getCode();
    }

    @Override
    public String encrypt(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        try {
            return aes.encryptHex(value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return value;
    }

    @Override
    public String decrypt(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        try {
            return aes.decryptStr(value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return value;
    }
}
