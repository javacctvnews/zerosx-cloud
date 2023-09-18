package com.zerosx.encrypt2.core.encryptor;

/**
 * 加解密算法器
 */
public interface IEncryptor {

    /**
     * 加密
     *
     * @param content 需要加密内容
     * @return 加密后内容
     */
    String encrypt(String content);

    /**
     * 解密
     *
     * @param content 需要解密内容
     * @return 解密后内容
     */
    String decrypt(String content);
}
