package com.zerosx.common.encrypt2.core.encryptor;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.zerosx.common.encrypt2.core.ICustomEncryptor;
import com.zerosx.common.encrypt2.enums.AlgorithmEnum;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class Sm4Encryptor implements ICustomEncryptor {

    private final SM4 sm4;

    public Sm4Encryptor(String password) {
        this.sm4 = SmUtil.sm4(password.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getAlgorithm() {
        return AlgorithmEnum.SM4.getCode();
    }

    @Override
    public String encrypt(String value) {
        try {
            String encrypted = sm4.encryptHex(value);
            log.debug("【{}】加密后:{}", value, encrypted);
            return encrypted;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return value;
    }

    @Override
    public String decrypt(String value) {
        try {
            String decrypted = sm4.decryptStr(value);
            log.debug("{}:解密后【{}】", value, decrypted);
            return decrypted;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return value;
    }

}
