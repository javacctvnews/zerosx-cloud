package com.zerosx.api.resource.factory;

import com.zerosx.api.resource.ISysDictDataClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SysDictDataFallbackFactory
 * @Description
 * @Author javacctvnews
 * @Date 2023/3/21 18:03
 * @Version 1.0
 */
@Component
@Slf4j
public class SysDictDataFallback implements FallbackFactory<ISysDictDataClient> {

    @Override
    public ISysDictDataClient create(Throwable cause) {
        return new ISysDictDataClient() {

            @Override
            public ResultVO<Map<Object, String>> getDictData(String dictType) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.emptyData();
            }

            @Override
            public ResultVO<List<I18nSelectOptionVO>> getDictList(String dictType) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.emptyData();
            }
        };
    }
}
