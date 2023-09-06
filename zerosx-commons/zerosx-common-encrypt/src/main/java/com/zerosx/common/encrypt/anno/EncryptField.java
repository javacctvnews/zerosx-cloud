package com.zerosx.common.encrypt.anno;

import com.zerosx.common.encrypt.core.ICustomEncryptor;
import com.zerosx.common.encrypt.core.encryptor.Sm4Encryptor;

import java.lang.annotation.*;

/**
 * 字段加密注解
 */
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

    /**
     * 预留字段
     *
     * @return
     */
    String key() default "";

    /**
     * 加解密算法
     *
     * @return
     */
    Class<? extends ICustomEncryptor> algo() default Sm4Encryptor.class;

}
