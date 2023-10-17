package com.zerosx.api.resource;

import com.zerosx.api.resource.factory.RegionClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 行政区域api
 */
@FeignClient(contextId = "IRegionClient", name = ServiceIdConstants.RESOURCE, fallbackFactory = RegionClientFallback.class)
public interface IRegionClient {

    /**
     * 按行政区域码获取名称
     *
     * @param areaCode
     * @return
     */
    @PostMapping("/area_name")
    ResultVO<String> getAreaName(@RequestParam("areaCode") String areaCode);

}
