package com.zerosx.common.security.utils;

import com.zerosx.common.base.constants.SecurityConstants;
import org.springframework.security.core.Authentication;

import java.util.Map;

public class SecurityUtils {

    /**
     * 获取用户类型
     *
     * @param authentication
     * @return
     */
    public static String getAuthUserType(Authentication authentication) {
        if (authentication == null || authentication.getDetails() == null) {
            return "";
        }
        Map<String, String> detailsMap = (Map<String, String>) authentication.getDetails();
        return detailsMap.get(SecurityConstants.USER_AUTH_TYPE);
    }

}
