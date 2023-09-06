package com.zerosx.system.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
public class SysDictTypeVO implements Serializable {

    private static final long serialVersionUID = 7653912715762206697L;

    @Schema(description = "字典类型ID")
    @ExcelProperty(value = {"字典类型ID"})
    private Long id;

    @Schema(description = "字典名称")
    @ExcelProperty(value = {"字典名称"})
    private String dictName;

    @Schema(description = "字典类型")
    @ExcelProperty(value = {"字典类型编码"})
    private String dictType;

    @Schema(description = "状态（0正常 1停用）")
    @ExcelProperty(value = {"状态"})
    private String dictStatus;

    @Schema(description = "备注")
    @ExcelProperty(value = {"备注"})
    private String remarks;

    @Schema(description = "创建人")
    @ExcelProperty(value = {"创建人"})
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "修改人")
    @ExcelProperty(value = {"修改人"})
    private String updateBy;

    @Schema(description = "修改时间")
    @ExcelProperty(value = {"修改时间"})
    private Date updateTime;



}
