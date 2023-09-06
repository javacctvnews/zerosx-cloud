package com.zerosx.api.system;

import com.zerosx.api.system.factory.SysDictDataFallbackFactory;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 行政区域api
 */
@FeignClient(name = ServiceIdConstants.SYSTEM, contextId = ServiceIdConstants.SYSTEM, fallbackFactory = SysDictDataFallbackFactory.class)
public interface IRegionControllerApi {

    @PostMapping("/areas/batch_query")
    ResultVO<Map<String, Object>> batchQueryAreas(@RequestBody List<String> areaCodes);

}
