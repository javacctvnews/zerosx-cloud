package com.zerosx.encrypt2.anno;

import java.lang.annotation.*;

/**
 * map入参有多个加密字段
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptFields {

    /**
     * 敏感信息
     */
    EncryptField[] value() default {};

}
