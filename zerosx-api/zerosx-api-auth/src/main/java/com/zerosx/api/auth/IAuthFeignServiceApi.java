package com.zerosx.api.auth;

import com.zerosx.api.auth.factory.OauthFeignServiceFallbackFactory;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = ServiceIdConstants.AUTH, contextId = ServiceIdConstants.AUTH, fallbackFactory = OauthFeignServiceFallbackFactory.class)
public interface IAuthFeignServiceApi {

    /**
     * 授权token
     *
     * @param multiValueMap
     * @return
     */
    @PostMapping(value = "/oauth/token", headers = {"Content-type:multipart/form-data"})
    ResultVO postAccessToken(@RequestBody MultiValueMap<String, String> multiValueMap);


}
