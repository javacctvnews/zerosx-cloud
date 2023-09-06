package com.zerosx.common.base.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 翻译，包括枚举、数据字典、数据库或接口
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Trans {

    /**
     * 获取翻译类型，
     * dict: 字典
     * enums: 枚举
     * rpc: feign服务
     *
     * @return 类型
     */
    String type();

    /**
     * 字段 比如  要翻译男女 上面的type写dict 此key写sex即可
     * dict: 字典的code
     * enums: 枚举的Class全限定名称
     * feign: ""
     *
     * @return
     */
    String key() default "";

    /**
     * 设置到的target value  比如我有一个sex字段，有一个sexName 字段  sex是0 设置ref翻译服务可以自动把sexname设置为男
     * 目标类字段配置了多个 有teacherName,teacherage 两个字段   我想要teacherName  可以写 teacherName#name
     *
     * @return
     */
    String ref() default "";

    /**
     * ref 支持多个，为了保持兼容新加了一个字段
     * 作用同ref 只是支持多个
     *
     * @return
     */
    String[] refs() default {};


}
