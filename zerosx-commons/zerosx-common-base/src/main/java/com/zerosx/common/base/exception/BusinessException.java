package com.zerosx.common.base.exception;


import com.zerosx.common.base.enums.ResultEnum;

public class BusinessException extends RuntimeException{

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultEnum.FAIL.getCode();
    }


    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
