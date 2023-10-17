package com.zerosx.resource.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典数据表
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_sys_dict_data")
public class SysDictData extends SuperEntity<SysDictData> {

    @Schema(description = "字典数据排序")
    private Integer dictSort;

    @Schema(description = "字典数据标签")
    private String dictLabel;

    @Schema(description = "字典数据键值")
    private String dictValue;

    @Schema(description = "字典类型编码")
    private String dictType;

    @Schema(description = "是否默认（Y是 N否）")
    private String isDefault;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "回显样式")
    private String listClass;

    @Schema(description = "样式属性")
    private String cssClass;

    @TableLogic
    private Integer deleted;
}
