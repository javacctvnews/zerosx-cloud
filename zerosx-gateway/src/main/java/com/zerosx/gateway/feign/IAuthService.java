package com.zerosx.gateway.feign;

import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.gateway.feign.fallback.AuthServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description 授权auth内部的feign api
 * @Author javacctvnews
 * @Date 2023/3/13 13:26
 */
@FeignClient(name = ServiceIdConstants.AUTH, contextId = ServiceIdConstants.AUTH, decode404 = true, fallbackFactory = AuthServiceFallbackFactory.class)
public interface IAuthService {

    /**
     * 获取client信息
     *
     * @param clientId
     * @return
     */
    @GetMapping("/oauth_client_details/{clientId}")
    ResultVO<OauthClientDetailsBO> getClient(@PathVariable("clientId") String clientId);

}
