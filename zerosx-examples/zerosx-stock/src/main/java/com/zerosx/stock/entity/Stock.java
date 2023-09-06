package com.zerosx.stock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName("t_stock")
@Data
public class Stock extends Model<Stock> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String commodityCode;

    private String name;

    private Integer count;

}
