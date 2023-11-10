package com.zerosx.common.sas.properties;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionProperties {

    /**
     * 是否开启url级别权限
     */
    private Boolean urlPermissionEnable = true;

    /**
     * 需要鉴权的客户端（oauth_client_details）
     * 默认都需要
     */
    private List<String> passClientIds = new ArrayList<>();

    /**
     * 不需要检查权限的url
     * 默认都需要
     */
    private List<String> passPermsUrls = new ArrayList<>();


}
