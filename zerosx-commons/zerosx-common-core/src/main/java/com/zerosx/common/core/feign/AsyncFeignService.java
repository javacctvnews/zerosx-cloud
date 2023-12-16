package com.zerosx.common.core.feign;

import com.zerosx.api.resource.IOssFileUploadClient;
import com.zerosx.api.resource.IRegionClient;
import com.zerosx.api.resource.ISysDictDataClient;
import com.zerosx.api.system.IMutiTenancyGroupClient;
import com.zerosx.api.system.ISysDeptClient;
import com.zerosx.api.system.vo.MutiTenancyGroupBO;
import com.zerosx.common.base.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
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
    private IOssFileUploadClient ossFileUploadClient;
    @Lazy
    @Autowired
    private IMutiTenancyGroupClient mutiTenancyGroupClient;
    @Lazy
    @Autowired
    private IRegionClient regionClient;
    @Lazy
    @Autowired
    private ISysDictDataClient sysDictDataClient;
    @Lazy
    @Autowired
    private ISysDeptClient sysDeptClient;

    @Async
    public Future<ResultVO<String>> getTenantName(String operatorId) {
        return new AsyncResult<>(mutiTenancyGroupClient.transIdName(operatorId));
    }

    @Async
    public Future<ResultVO<List<I18nSelectOptionVO>>> getDictList(String dictType) {
        return new AsyncResult<>(sysDictDataClient.getDictList(dictType));
    }

    @Async
    public Future<ResultVO<String>> getObjectViewUrl(String objectName) {
        return new AsyncResult<>(ossFileUploadClient.getObjectViewUrl(objectName));
    }

    @Async
    public Future<ResultVO<String>> getAreaName(String areaCode) {
        return new AsyncResult<>(regionClient.getAreaName(areaCode));
    }

    @Async
    public Future<ResultVO<String>> getDeptName(Long deptId) {
        return new AsyncResult<>(sysDeptClient.queryName(deptId));
    }

    @Async
    public Future<ResultVO<MutiTenancyGroupBO>> queryOperator(String operatorId) {
        return new AsyncResult<>(mutiTenancyGroupClient.queryOperator(operatorId));
    }
}
