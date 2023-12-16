package com.zerosx.api.resource.factory;

import com.zerosx.api.resource.IDGenClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class IDGenClientFallback implements FallbackFactory<IDGenClient> {

    @Override
    public IDGenClient create(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return new IDGenClient() {
            @Override
            public ResultVO<Long> segment(String bizTag) {
                return ResultVOUtil.feignFail("分布式ID生成失败:" + bizTag);
            }
        };
    }

}
