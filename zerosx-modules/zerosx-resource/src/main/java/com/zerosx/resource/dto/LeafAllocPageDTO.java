package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 美团分布式ID
 * @Description
 * @author javacctvnews
 * @date 2023-12-05 14:00:46
 */
@Getter
@Setter
@Schema(description = "美团分布式ID:分页查询DTO")
public class LeafAllocPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bizTag;

}
