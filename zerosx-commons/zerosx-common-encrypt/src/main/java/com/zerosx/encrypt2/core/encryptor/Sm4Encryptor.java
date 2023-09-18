package com.zerosx.encrypt2.core.encryptor;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * Sm4Encryptor
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-13 10:38
 **/
@Slf4j
public class Sm4Encryptor extends AbsEncryptor {

    private final SM4 sm4;

    public Sm4Encryptor(String password) {
        this.sm4 = SmUtil.sm4(password.getBytes(StandardCharsets.UTF_8));
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
