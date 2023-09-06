package com.zerosx.common.encrypt2.anno;

import com.zerosx.common.encrypt2.core.ICustomEncryptor;
import com.zerosx.common.encrypt2.core.encryptor.Sm4Encryptor;
import com.zerosx.common.encrypt2.enums.EncryptMode;

import java.lang.annotation.*;

/**
 * Map对象敏感信息
 *
 * @author : wdh
 * @since : 2022/5/23 15:15
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptKey {

    /**
     * map中的key名
     */
    String key() default "";

    /**
     * 加密规则
     */
    Class<? extends ICustomEncryptor> rule() default Sm4Encryptor.class;

    /**
     * 加解密方式
     */
    EncryptMode mode() default EncryptMode.ALL;
}
