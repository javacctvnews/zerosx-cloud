package com.zerosx.common.log.annotation;

import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.common.log.enums.OperatorType;

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
public @interface SystemLog {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 按钮名称或者按钮业务名称
     */
    String btnName() default "";

    /**
     * 功能类别
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};

}
