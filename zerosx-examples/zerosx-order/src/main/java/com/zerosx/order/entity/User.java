package com.zerosx.order.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.core.encryptor.AesEncryptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 加解密DEMO
 *
 * @author javacctvnews
 * @Description
 * @date 2023-09-22 15:32:00
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
@EncryptClass
public class User extends SuperEntity<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    @EncryptField(algo = AesEncryptor.class)
    private String userName;

    /**
     * 手机号码
     */
    @EncryptField
    private String phone;

    /**
     * 电子邮箱
     */
    @EncryptField(algo = AesEncryptor.class)
    private String email;

    /**
     * 身份证号码
     */
    @EncryptField
    private String cardId;

    /**
     * 家庭地址
     */
    @EncryptField
    private String address;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

}
