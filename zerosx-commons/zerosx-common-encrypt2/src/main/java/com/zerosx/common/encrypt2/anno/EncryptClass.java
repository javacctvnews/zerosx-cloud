package com.zerosx.common.encrypt2.anno;

import java.lang.annotation.*;

/**
 * 敏感信息类，带有此注解的类才会加解密敏感字段
 * @date 2023-08-25
 */
@Inherited
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptClass {


}
