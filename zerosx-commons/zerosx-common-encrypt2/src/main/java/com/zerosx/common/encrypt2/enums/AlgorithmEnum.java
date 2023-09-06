package com.zerosx.common.encrypt2.enums;

import com.zerosx.common.encrypt2.core.ICustomEncryptor;
import com.zerosx.common.encrypt2.core.encryptor.AesEncryptor;
import com.zerosx.common.encrypt2.core.encryptor.Sm4Encryptor;
import lombok.Getter;

@Getter
public enum AlgorithmEnum {
    /**
     * aes加密算法
     */
    AES("aes", "AES", AesEncryptor.class),
    /**
     * sm4
     */
    SM4("sm4", "SM4", Sm4Encryptor.class);

    private final String code;

    private final String name;

    private final Class<? extends ICustomEncryptor> clazz;

    AlgorithmEnum(String code, String name, Class<? extends ICustomEncryptor> clazz) {
        this.code = code;
        this.name = name;
        this.clazz = clazz;
    }

    public static Class<? extends ICustomEncryptor> getAlgorithm(String algorithmType) {
        AlgorithmEnum[] values = AlgorithmEnum.values();
        for (AlgorithmEnum algorithmEnum : values) {
            if (algorithmEnum.getCode().equals(algorithmType)) {
                return algorithmEnum.getClazz();
            }
        }
        return null;
    }
}
