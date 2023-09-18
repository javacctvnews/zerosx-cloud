package com.zerosx.common.base.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * OauthClientDetailsBO
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-18 15:49
 **/
@Data
public class OauthClientDetailsBO implements Serializable {

    private static final long serialVersionUID = 586352283160208409L;

    private String clientId;

    private String clientName;

    private String resourceIds;

    private String scope;

    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private String additionalInformation;

}
