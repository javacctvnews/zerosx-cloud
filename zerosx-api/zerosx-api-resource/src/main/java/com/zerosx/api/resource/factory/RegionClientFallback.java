package com.zerosx.api.resource.factory;

import com.zerosx.api.resource.IRegionClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * RegionControllerFallbackFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-24 16:53
 **/
@Component
@Slf4j
public class RegionClientFallback implements FallbackFactory<IRegionClient> {

    @Override
    public IRegionClient create(Throwable throwable) {
        return new IRegionClient() {

            @Override
            public ResultVO<String> getAreaName(String areaCode) {
                log.error(throwable.getMessage(), throwable);
                return ResultVOUtil.feignFail(StringUtils.EMPTY);
            }
        };
    }
}
