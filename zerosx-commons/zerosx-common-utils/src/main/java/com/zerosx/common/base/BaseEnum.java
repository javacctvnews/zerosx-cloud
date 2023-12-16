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

    /**
     * 回显CSS样式
     * @return css
     */
    String getCss();

}
