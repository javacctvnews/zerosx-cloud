package com.zerosx.common.log.feign.fallback;

import com.zerosx.common.log.feign.ISysOperatorLogClient;
import com.zerosx.common.log.vo.SystemOperatorLogBO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName SystemOperatorLogServiceFallbackFactor
 * @Description
 * @Author javacctvnews
 * @Date 2023/4/2 14:32
 * @Version 1.0
 */
@Component
@Slf4j
public class SystemOperatorLogFallbackFactory implements FallbackFactory<ISysOperatorLogClient> {

    @Override
    public ISysOperatorLogClient create(Throwable cause) {
        return new ISysOperatorLogClient() {
            @Override
            public ResultVO<?> add(SystemOperatorLogBO systemOperatorLogBO) {
                log.error("日志服务调用失败:{}", cause.getMessage());
                return ResultVOUtil.error(cause.getMessage());
            }
        };
    }
}
