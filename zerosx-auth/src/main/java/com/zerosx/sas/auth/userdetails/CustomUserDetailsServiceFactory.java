package com.zerosx.sas.auth.userdetails;

import com.zerosx.sas.auth.grant.CustomOAuth2ErrorCodes;
import com.zerosx.sas.utils.SasAuthUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomUserDetailsServiceFactory {

    @Autowired(required = false)
    private List<ICustomUserDetailsService> customUserDetailsServices;

    private final Map<String, ICustomUserDetailsService> customUserDetailsMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initDetailsService() {
        for (ICustomUserDetailsService detailsService : customUserDetailsServices) {
            customUserDetailsMap.put(detailsService.authUserType(), detailsService);
        }
    }

    /**
     * 获取指定鉴权用户类型的实现
     *
     * @param authentication
     * @return
     */
    public ICustomUserDetailsService getDetailsService(Authentication authentication) {
        String authUserType = SasAuthUtils.getAuthUserType(authentication);
        ICustomUserDetailsService customUserDetailsService = customUserDetailsMap.get(authUserType);
        if (customUserDetailsService == null) {
            SasAuthUtils.throwError(CustomOAuth2ErrorCodes.UNSUPPORTED_USER_AUTH_TYPE, "找不到鉴权用户类型为 【" + authUserType + "】 的实现类");
        }
        return customUserDetailsService;
    }

    public ICustomUserDetailsService getDetailsService(String authUserType) {
        ICustomUserDetailsService customUserDetailsService = customUserDetailsMap.get(authUserType);
        if (customUserDetailsService == null) {
            SasAuthUtils.throwError(CustomOAuth2ErrorCodes.UNSUPPORTED_USER_AUTH_TYPE, "找不到鉴权用户类型为 【" + authUserType + "】 的实现类");
        }
        return customUserDetailsService;
    }
}
