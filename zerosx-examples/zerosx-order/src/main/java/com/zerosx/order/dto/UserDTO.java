package com.zerosx.order.dto;

import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 加解密DEMO
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 15:32:00
 */
@Getter
@Setter
@Schema(description = "加解密DEMODTO")
@EncryptClass
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="自增ID")
    private Long id;

    @Schema(description ="昵称")
    private String nickName;

    @Schema(description ="真实姓名")
    private String userName;

    @Schema(description ="手机号码")
    @EncryptField
    private String phone;

    @Schema(description ="电子邮箱")
    private String email;

    @Schema(description ="身份证号码")
    private String cardId;

    @Schema(description ="家庭地址")
    private String address;

}
