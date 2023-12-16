package com.zerosx.api.system.factory;

import com.zerosx.api.system.ISysDeptClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SysDeptClientFallback implements FallbackFactory<ISysDeptClient> {

    @Override
    public ISysDeptClient create(Throwable cause) {
        return new ISysDeptClient() {
            @Override
            public ResultVO<String> queryName(Long id) {
                log.error("查询部门名称异常", cause);
                return ResultVOUtil.feignFail(cause.getMessage());
            }
        };
    }
}
