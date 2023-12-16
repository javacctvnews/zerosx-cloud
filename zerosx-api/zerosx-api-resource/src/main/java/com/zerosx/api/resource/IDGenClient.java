package com.zerosx.api.resource;

import com.zerosx.api.resource.factory.IDGenClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(contextId = "IDGenClient", name = ServiceIdConstants.RESOURCE, fallbackFactory = IDGenClientFallback.class)
public interface IDGenClient {

    /**
     * 生成分布式ID
     *
     * @param bizTag 业务标识
     * @return 分布式ID
     */
    @GetMapping(value = "/segment/{bizTag}")
    ResultVO<Long> segment(@PathVariable("bizTag") String bizTag);

}
