package com.zerosx.order.vo;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;

/**
 * 加解密DEMO
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 15:32:00
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@Schema(description = "加解密DEMO:分页结果对象")
public class UserPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增ID")
    private Long id;

    @Schema(description = "昵称")
    @ExcelProperty(value = {"昵称"})
    private String nickName;

    @Schema(description = "真实姓名")
    @ExcelProperty(value = {"真实姓名"})
    private String userName;

    @Schema(description = "手机号码")
    @ExcelProperty(value = {"手机号码"})
    private String phone;

    @Schema(description = "电子邮箱")
    @ExcelProperty(value = {"电子邮箱"})
    private String email;

    @Schema(description = "身份证号码")
    @ExcelProperty(value = {"身份证号码"})
    private String cardId;

    @Schema(description = "家庭地址")
    @ExcelProperty(value = {"家庭地址"})
    private String address;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "创建人")
    @ExcelProperty(value = {"创建人"})
    private String createBy;

    @Schema(description = "更新时间")
    @ExcelProperty(value = {"更新时间"})
    private Date updateTime;

    @Schema(description = "更新人")
    @ExcelProperty(value = {"更新人"})
    private String updateBy;

}
