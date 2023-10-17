package com.zerosx.common.base;

/**
 * 枚举需实现此接口
 */
public interface BaseEnum<T> {

    /**
     * code类型
     *
     * @return code
     */
    T getCode();

    /**
     * message
     *
     * @return message
     */
    String getMessage();

}
