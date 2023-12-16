package com.zerosx.resource.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
public class SysDictDataVO implements Serializable {

    private static final long serialVersionUID = 8397428695970415688L;

    @Schema(description = "字典数据表CODE")
    @ExcelIgnore
    private Long id;

    @Schema(description = "字典类型:关联sys_dict_type")
    @ExcelProperty(value = {"字典类型编码"})
    private String dictType;

    @Schema(description = "字典标签")
    @ExcelProperty(value = {"字典标签"})
    private String dictLabel;

    @Schema(description = "字典键值")
    @ExcelProperty(value = {"字典键值"})
    private String dictValue;

    @Schema(description = "字典排序")
    @ExcelProperty(value = {"字典排序"})
    private Integer dictSort;

    @Schema(description = "状态（0正常 1停用）")
    @ExcelProperty(value = {"状态(0正常 1停用)"})
    private String status;

    @Schema(description = "备注")
    @ExcelProperty(value = {"备注"})
    private String remarks;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "是否默认（Y是 N否）")
    @ExcelProperty(value = {"是否默认(Y是 N否)"})
    private String isDefault;

    @Schema(description = "创建人")
    @ExcelProperty(value = {"创建人"})
    private String createBy;

    @Schema(description = "修改人")
    @ExcelProperty(value = {"修改人"})
    private String updateBy;

    @Schema(description = "修改时间")
    @ExcelProperty(value = {"修改时间"})
    private Date updateTime;

    @Schema(description = "回显样式")
    @ExcelIgnore
    private String listClass;

    @Schema(description = "样式属性")
    @ExcelIgnore
    private String cssClass;

}
