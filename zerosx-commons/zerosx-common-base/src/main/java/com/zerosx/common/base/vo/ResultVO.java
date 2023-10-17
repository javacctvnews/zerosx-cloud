package com.zerosx.common.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "统一响应对象")
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 5973857821271133451L;

    /**
     * 状态码，0：成功，其他失败
     */
    @Schema(description = "状态码")
    private Integer code;

    /**
     * 响应的描述信息
     */
    @Schema(description = "描述")
    private String msg;

    /**
     * 响应时间戳
     */
    @Schema(description = "时间戳")
    private Long timestamp;

    /**
     * 返回的数据
     */
    @Schema(description = "响应数据")
    private T data;


    public ResultVO(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
        this.data = data;
    }

    public ResultVO(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public ResultVO(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public Long getTimestamp() {
        return System.currentTimeMillis();
    }

    public boolean success() {
        return this.code != null && this.code == 0;
    }

    /**
     * 检查是否有异常
     * @return T
     */
    @JsonIgnore
    public T checkException() {
        if (success()) {
            return this.data;
        } else {
            //非0表示有异常
            throw new BusinessException(this.code, this.msg);
        }
    }

}
