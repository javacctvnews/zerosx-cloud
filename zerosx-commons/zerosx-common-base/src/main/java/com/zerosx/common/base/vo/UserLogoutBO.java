package com.zerosx.common.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserLogoutBO implements Serializable {

    private static final long serialVersionUID = 4363086366675231628L;

    private String clientId = "saas";//默认

    private String userName;

}
