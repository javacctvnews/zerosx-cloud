package com.zerosx.resource.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsCodeDTO implements Serializable {

    private String code2;

    private String uuid;

    private String mobilePhone;

}
