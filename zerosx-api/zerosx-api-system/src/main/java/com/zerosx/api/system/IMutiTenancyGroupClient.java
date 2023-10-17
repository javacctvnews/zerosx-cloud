package com.zerosx.api.system;

import com.zerosx.api.system.factory.MutiTenancyGroupClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "IMutiTenancyGroupClient", name = ServiceIdConstants.SYSTEM, fallbackFactory = MutiTenancyGroupClientFallback.class)
public interface IMutiTenancyGroupClient {

    @PostMapping("/muti_tenancy/tenantName")
    ResultVO<String> transIdName(@RequestParam(value = "operatorId", required = false) String operatorId);

}
