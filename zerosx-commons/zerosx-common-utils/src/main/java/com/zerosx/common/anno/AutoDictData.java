package com.zerosx.common.anno;

import java.lang.annotation.*;

/**
 * @ClassName AutoDictData
 * @Description 有此注解的枚举将会自动保存到数据库
 * @Author javacctvnews
 * @Date 2023/8/31 14:21
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoDictData {
    /**
     * 数据字典类型编码
     *
     * @return code
     */
    String code() default "";

    /**
     * 数据字典类型名称
     *
     * @return name
     */
    String name() default "";

    /**
     * 字典类型 业务描述
     * @return desc
     */
    String desc() default "";

}
