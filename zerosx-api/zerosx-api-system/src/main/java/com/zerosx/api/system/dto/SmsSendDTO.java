package com.zerosx.api.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Data
@Schema(description = "短信发送DTO")
public class SmsSendDTO {

    @Schema(description = "短信业务类型码")
    @NotBlank(message = "短信业务类型码不能为空")
    private String businessCode;

    @Schema(description = "运营商ID")
    //@NotBlank(message = "运营商ID不能为空")
    private String operatorId;

    @Schema(description = "手机号码,多个逗号分隔")
    @NotBlank(message = "手机号码不能为空")
    private String phoneNumbers;

    //@Schema(description = "短信模板占位符参数，必须是对象的json格式(templateParam、params二选一，优先级高于params)")
    //public String templateParam;

    @Schema(description = "短信模板占位符参数")
    @NotEmpty(message = "短信模板占位符参数不能为空")
    Map<String, String> params = new HashMap<>();

}
