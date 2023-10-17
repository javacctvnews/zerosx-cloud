package com.zerosx.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.io.Serializable;

/**
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 14:09:54
 */
@Getter
@Setter
@Schema(description = "用户订单:分页查询DTO")
public class UserOrderPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

}
