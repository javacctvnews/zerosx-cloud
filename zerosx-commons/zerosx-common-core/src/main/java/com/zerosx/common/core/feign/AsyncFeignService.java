package com.zerosx.common.core.feign;

import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @ClassName AsyncFeignService
 * @Description 异步调用
 * @Author javacctvnews
 * @Date 2023/5/25 22:00
 * @Version 1.0
 */
@Component
@Slf4j
public class AsyncFeignService {

    @Lazy
    @Autowired
    private ISystemFeignService systemFeignService;

    @Async
    public Future<ResultVO<String>> getTenantName(String operatorId) {
        return new AsyncResult<>(systemFeignService.transIdName(operatorId));
    }

    @Async
    public Future<ResultVO<Map<String, String>>> getDictData(String dictType) {
        return new AsyncResult<>(systemFeignService.getDictData(dictType));
    }

    @Async
    public Future<ResultVO<String>> getObjectViewUrl(String objectName) {
        return new AsyncResult<>(systemFeignService.getObjectViewUrl(objectName));
    }

    @Async
    public Future<ResultVO<String>> getAreaName(String areaCode) {
        return new AsyncResult<>(systemFeignService.getAreaName(areaCode));
    }
}
