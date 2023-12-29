package com.zerosx.idempotent.anno;

import com.zerosx.idempotent.enums.IdempotentTypeEnum;

import java.lang.annotation.*;

/**
 * 幂等注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 验证幂等类型，支持多种幂等方式
     * Rest 建议使用 {@link IdempotentTypeEnum#TOKEN} 或 {@link IdempotentTypeEnum#PARAM}
     * 其它类型幂等验证，使用 {@link IdempotentTypeEnum#SPEL}
     */
    IdempotentTypeEnum type();

    /**
     * spEL表达式，只有在 {@link Idempotent#type()} 为 {@link IdempotentTypeEnum#SPEL} 时生效
     */
    String spEL() default "";

    /**
     * 触发幂等失败逻辑时的提示语
     */
    String msg() default "请求已提交处理中，请勿重复操作";

}
