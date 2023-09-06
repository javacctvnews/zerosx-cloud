package com.zerosx.common.encrypt.core.encryptor;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.zerosx.common.encrypt.core.ICustomEncryptor;
import com.zerosx.common.encrypt.enums.AlgorithmEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isBlank(value)) {
            return value;
        }
        try {
            return sm4.encryptHex(value);
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
            return sm4.decryptStr(value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return value;
    }

}
