package com.zerosx.common.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 前端select下拉框对象
 * @Author javacctvnews
 * @Date 2023/3/13 12:40
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "前端select下拉框对象")
public class SelectOptionVO implements Serializable {

    private static final long serialVersionUID = -1101932060253858296L;

    @Schema(description = "选项的值")
    private Object value;

    @Schema(description = "选项的标签")
    private String label;

    @Schema(description = "选项子集合")
    private List<SelectOptionVO> children;

    public SelectOptionVO(Object value, String label) {
        this.value = value;
        this.label = label;
    }
}
