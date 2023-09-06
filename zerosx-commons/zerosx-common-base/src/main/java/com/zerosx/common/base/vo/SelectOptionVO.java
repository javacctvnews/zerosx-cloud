package com.zerosx.common.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 前端select下拉框对象
 * @Author javacctvnews
 * @Date 2023/3/13 12:40
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "前端select下拉框对象")
public class SelectOptionVO implements Serializable {

    private static final long serialVersionUID = -1101932060253858296L;

    @Schema(description = "选项的值")
    private Object value;

    @Schema(description = "选项的标签")
    private String label;

    @Schema(description = "选项子集合")
    private List<SelectOptionVO> children;

}
