package com.zerosx.common.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页查询
 */
@Getter
@Setter
@Schema(description = "分页查询")
public class RequestVO<T> {

    @Schema(description = "当前第几页", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize = 20;

    @Schema(description = "排序列")
    private List<SortVO> sortList;

    @Schema(description = "查询条件对象")
    private T t;

    @Setter
    @Getter
    @Schema(description = "排序对象")
    public static class SortVO {

        @Schema(description = "排序列", example = "createTime", requiredMode = Schema.RequiredMode.REQUIRED)
        private String orderByColumn;

        @Schema(description = "排序顺序", allowableValues = {"descending", "ascending"}, example = "descending", requiredMode = Schema.RequiredMode.REQUIRED)
        private String sortType;

    }

}
