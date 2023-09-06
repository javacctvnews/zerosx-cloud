package com.zerosx.common.base.enums;

/**
 * @author javacctvnews
 */
public interface CodeEnum<T> {
    /**
     * code类型
     * @return code
     */
    T getCode();

    /**
     * message
     * @return message
     */
    String getMessage();
}
