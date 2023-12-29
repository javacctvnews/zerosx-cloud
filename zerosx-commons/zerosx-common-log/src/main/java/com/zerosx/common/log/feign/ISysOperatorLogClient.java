package com.zerosx.common.log.feign;

import com.zerosx.common.log.feign.fallback.SystemOperatorLogFallbackFactory;
import com.zerosx.common.log.vo.SystemOperatorLogBO;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName ISystemOperatorLogService
 * @Description
 * @Author javacctvnews
 * @Date 2023/4/2 14:31
 * @Version 1.0
 */
@FeignClient(value = ServiceIdConstants.SYSTEM, contextId = ServiceIdConstants.SYSTEM, fallbackFactory = SystemOperatorLogFallbackFactory.class)
public interface ISysOperatorLogClient {

    /**
     * 保存操作日志
     *
     * @param systemOperatorLogBO
     * @return
     */
    @PostMapping("/system_operator_log/save")
    ResultVO<?> add(@RequestBody SystemOperatorLogBO systemOperatorLogBO);

}
