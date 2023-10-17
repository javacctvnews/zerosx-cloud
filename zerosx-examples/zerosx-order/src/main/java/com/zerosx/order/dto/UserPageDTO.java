package com.zerosx.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.io.Serializable;

/**
 * 加解密DEMO
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 15:32:00
 */
@Getter
@Setter
@Schema(description = "加解密DEMO:分页查询DTO")
public class UserPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

}
