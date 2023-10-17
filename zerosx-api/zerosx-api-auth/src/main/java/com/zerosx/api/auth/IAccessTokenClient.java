package com.zerosx.api.auth;

import com.zerosx.api.auth.factory.AccessTokenClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(contextId = "IAccessTokenClient", name = ServiceIdConstants.AUTH,
        fallbackFactory = AccessTokenClientFallback.class)
public interface IAccessTokenClient {

    /**
     * 授权token
     *
     * @param multiValueMap
     * @return
     */
    @PostMapping(value = "/oauth/token", headers = {"Content-type:multipart/form-data"})
    ResultVO postAccessToken(@RequestBody MultiValueMap<String, String> multiValueMap);

    /**
     * 获取client信息
     *
     * @param clientId
     * @return
     */
    @GetMapping("/oauth_client_details/{clientId}")
    ResultVO<OauthClientDetailsBO> getClient(@PathVariable("clientId") String clientId);

}
