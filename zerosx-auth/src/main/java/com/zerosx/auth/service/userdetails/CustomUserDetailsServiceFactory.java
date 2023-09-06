package com.zerosx.auth.service.userdetails;

import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
        String authUserType = SecurityUtils.getAuthUserType(authentication);
        ICustomUserDetailsService customUserDetailsService = customUserDetailsMap.get(authUserType);
        if (customUserDetailsService == null) {
            throw new BusinessException(ResultEnum.UNAUTHORIZED.getCode(), "找不到鉴权用户类型为 【" + authUserType + "】 的实现类");
        }
        return customUserDetailsService;
    }

}
