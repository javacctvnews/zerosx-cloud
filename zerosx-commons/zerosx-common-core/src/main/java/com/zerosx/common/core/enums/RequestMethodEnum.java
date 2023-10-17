package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * RequestMethodEnum
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-25 20:14
 **/
@Getter
@AutoDictData(code = "RequestMethodEnum", name = "请求方法类别")
public enum RequestMethodEnum implements BaseEnum<String> {

    POST("post", "POST"),
    GET("get", "GET"),
    PUT("put", "PUT"),
    DELETE("delete", "DELETE"),
    ;
    private final String code;

    private final String message;

    RequestMethodEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
