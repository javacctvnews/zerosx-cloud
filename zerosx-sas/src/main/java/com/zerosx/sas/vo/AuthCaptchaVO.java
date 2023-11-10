package com.zerosx.sas.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthCaptchaVO {

    private Boolean captchaEnabled;

    private String uuid;

    private String img;//图片

}
