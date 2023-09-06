package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典类型表
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_sys_dict_type")
public class SysDictType extends SuperEntity<SysDictType> {

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "状态（0正常 1停用）")
    private String dictStatus;

    @Schema(description = "备注")
    private String remarks;

    @TableLogic
    private Integer deleted;

}
