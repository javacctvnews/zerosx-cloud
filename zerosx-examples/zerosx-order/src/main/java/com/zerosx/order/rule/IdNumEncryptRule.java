package com.zerosx.order.rule;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.zerosx.encrypt2.core.encryptor.AbsEncryptor;

import java.nio.charset.StandardCharsets;


/**
 * 身份证号加解密
 */
public class IdNumEncryptRule extends AbsEncryptor {


    private final SM4 sm4;

    public IdNumEncryptRule(String password) {
        this.sm4 = SmUtil.sm4(password.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 数据加密
     */
    public String encrypt(String content) {
        try {
            return sm4.encryptHex(content);
        } catch (Exception e) {
            return content;
        }
    }

    /**
     * 数据解密
     */
    public String decrypt(String content) {
        try {
            return sm4.decryptStr(content);
        } catch (Exception e) {
            return content;
        }
    }
}
