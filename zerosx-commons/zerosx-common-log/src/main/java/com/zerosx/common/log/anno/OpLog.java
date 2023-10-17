package com.zerosx.common.log.anno;

import com.zerosx.common.log.enums.OpTypeEnum;

import java.lang.annotation.*;

/**
 * @author javacctvnews
 * @description 自定义操作日志注解
 * @date Created in 2020/11/14 17:32
 * @modify by
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpLog {

    /**
     * 模块
     */
    String mod() default "";

    /**
     * 按钮名称或者按钮业务名称
     */
    String btn() default "";

    /**
     * 功能类别
     */
    OpTypeEnum opType() default OpTypeEnum.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean saveParams() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParams() default {};


}
