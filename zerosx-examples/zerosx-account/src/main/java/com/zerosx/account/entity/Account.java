package com.zerosx.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName("t_account")
@Data
public class Account extends Model<Account> {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userId;

    private Double amount;

}
