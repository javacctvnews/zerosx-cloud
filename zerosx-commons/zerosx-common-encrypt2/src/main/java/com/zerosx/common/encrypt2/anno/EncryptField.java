package com.zerosx.common.encrypt2.anno;

import com.zerosx.common.encrypt2.core.ICustomEncryptor;
import com.zerosx.common.encrypt2.core.encryptor.Sm4Encryptor;
import com.zerosx.common.encrypt2.enums.EncryptMode;

import java.lang.annotation.*;

/**
 * 敏感信息String类型（包括泛型为String的集合和数组）字段
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

    /**
     * 加解密规则
     */
    Class<? extends ICustomEncryptor> rule() default Sm4Encryptor.class;

    /**
     * 加解密方式
     */
    EncryptMode mode() default EncryptMode.ALL;
}
