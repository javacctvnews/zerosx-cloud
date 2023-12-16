package com.zerosx.resource.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@TableName("leaf_alloc")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class LeafAlloc extends Model<LeafAlloc> {

    @TableId
    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    private Date updateTime;

}
