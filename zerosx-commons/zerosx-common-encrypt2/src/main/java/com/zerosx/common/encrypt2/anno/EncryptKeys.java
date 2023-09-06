package com.zerosx.common.encrypt2.anno;

import java.lang.annotation.*;

/**
 * 敏感信息
 * 使用在Mapper层，支持多个参数、list参数、Map参数
 *
 * @date 2023/8/25
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptKeys {

    /**
     * 敏感信息 字段
     */
    EncryptKey[] value() default {};

}
