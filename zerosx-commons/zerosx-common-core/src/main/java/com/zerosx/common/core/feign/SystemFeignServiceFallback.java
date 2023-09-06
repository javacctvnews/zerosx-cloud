package com.zerosx.common.core.feign;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName RateFeignServiceFallback
 * @Description
 * @Author javacctvnews
 * @Date 2023/5/25 21:58
 * @Version 1.0
 */
@Slf4j
@Component
public class SystemFeignServiceFallback implements FallbackFactory<ISystemFeignService> {

    @Override
    public ISystemFeignService create(Throwable cause) {
        return new ISystemFeignService() {
            @Override
            public ResultVO<String> transIdName(String operatorId) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.feignFail(StringUtils.EMPTY);
            }

            @Override
            public ResultVO<Map<String, String>> getDictData(String dictType) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.feignFail(StringUtils.EMPTY);
            }

            @Override
            public ResultVO<String> getObjectViewUrl(String objectName) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.feignFail(StringUtils.EMPTY);
            }

            @Override
            public ResultVO<String> getAreaName(String areaCode) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.feignFail(StringUtils.EMPTY);
            }
        };
    }
}
