package com.zerosx.idempotent.enums;

/**
 * 幂等验证类型
 */
public enum IdempotentTypeEnum {

    /**
     * token 未实现 todo
     */
    TOKEN,

    /**
     * 方法参数
     * 比较适用的场景：
     * 1）不存在唯一性检查的情况
     */
    PARAM,

    /**
     * spEL表达式
     * 比较适用的场景：
     * 1）存在唯一性检查的情况（多个条件）
     */
    SPEL


}
