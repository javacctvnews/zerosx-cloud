package com.zerosx.common.encrypt.core;

/**
 * 加解密接口，方便扩展加解密方式
 */
public interface ICustomEncryptor {

    /**
     * 获得当前算法
     */
    String getAlgorithm();

    /**
     * 加密
     *
     * @param value 待加密字符串
     * @return 加密后的字符串
     */
    String encrypt(String value);

    /**
     * 解密
     *
     * @param value 待加密字符串
     * @return 解密后的字符串
     */
    String decrypt(String value);

}
