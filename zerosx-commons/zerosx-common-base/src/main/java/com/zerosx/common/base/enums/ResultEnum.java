package com.zerosx.common.base.enums;


import lombok.Getter;

@Getter
public enum ResultEnum {

    //成功
    SUCCESS(0, "成功"),

    //失败
    FAIL(10001, "失败"),
    //所有参数不符合要求均抛此异常
    PARAM_ERROR(10002, "业务参数不合法"),
    //服务内部调用异常
    FEIGN_EXCEPTION(10005, "远程调用异常"),
    //TOKEN失效
    UNAUTHORIZED(401, "无效的会话，或者会话已过期"),
    //没有权限
    FORBIDDEN(403, "您没有权限"),

    ;
    /**
     * 响应码
     */
    private final Integer code;
    /**
     * 响应信息描述
     */
    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
