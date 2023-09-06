package com.zerosx.common.encrypt.anno;

import java.lang.annotation.*;

/**
 * 注解敏感信息类的注解
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptClass {


}
