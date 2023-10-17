package com.zerosx.encrypt2.anno;


import com.zerosx.encrypt2.core.encryptor.IEncryptor;
import com.zerosx.encrypt2.core.encryptor.Sm4Encryptor;
import com.zerosx.encrypt2.core.enums.EncryptMode;

import java.lang.annotation.*;

/**
 * 加密字段注解或dao方法
 * 姓名、身份证号码、住址、电话、银行账号、邮箱、密码、医疗信息、教育背景等等
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

    /**
     * 加解密算法类
     */
    Class<? extends IEncryptor> algo() default Sm4Encryptor.class;

    /**
     * 加解密方式
     */
    EncryptMode mode() default EncryptMode.ALL;

    /**
     * 密钥，为空时使用全局密钥
     *
     * @return
     */
    String password() default "";

    /**
     * 字段名，场景：
     * 1）参数是map
     * 2）返回结果是map
     *
     * @return
     */
    String field() default "";

}
