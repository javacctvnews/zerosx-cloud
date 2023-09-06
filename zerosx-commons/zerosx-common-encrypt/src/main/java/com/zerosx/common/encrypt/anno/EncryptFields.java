package com.zerosx.common.encrypt.anno;

import java.lang.annotation.*;

/**
 * 多个String敏感信息
 *
 * @author : wdh
 * @since : 2022/5/23 15:15
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptFields {

    /**
     * 敏感信息
     */
    EncryptField[] value() default {};

}
