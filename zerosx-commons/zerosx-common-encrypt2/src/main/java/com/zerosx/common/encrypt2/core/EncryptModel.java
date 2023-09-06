package com.zerosx.common.encrypt2.core;

import lombok.Data;

@Data
public class EncryptModel {

    /**
     * 加密字段
     */
    private String field;

    /**
     * 加密规则
     */
    private Class<? extends ICustomEncryptor> rule;

}
