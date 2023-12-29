package com.zerosx.common.core.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ClearCache {

    /**
     * 键名
     */
    String keys();

    /**
     * 字段名
     */
    String field() default "";

    /**
     * 延时多少毫秒后删除
     */
    long delayTime() default 3000;

}
