package com.zerosx.order.algo;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.zerosx.encrypt2.core.encryptor.AbsEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * 手机号加解密
 */
@Slf4j
public class PhoneEncryptRule extends AbsEncryptor {

    private final AES aes;

    public PhoneEncryptRule(String password) {
        if (StringUtils.isBlank(password)) {
            throw new RuntimeException("AES加密密码不能为空");
        }
        this.aes = SecureUtil.aes(password.getBytes(StandardCharsets.UTF_8));
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
