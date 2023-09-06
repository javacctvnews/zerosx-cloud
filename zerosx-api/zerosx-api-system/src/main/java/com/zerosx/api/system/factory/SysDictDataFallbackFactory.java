package com.zerosx.api.system.factory;

import com.zerosx.api.system.ISysDictDataApi;
import com.zerosx.api.system.vo.I18nSelectOptionVO;
import com.zerosx.common.base.utils.ResultVOUtil;
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
public class SysDictDataFallbackFactory implements FallbackFactory<ISysDictDataApi> {

    @Override
    public ISysDictDataApi create(Throwable cause) {
        return new ISysDictDataApi() {
            @Override
            public ResultVO<List<I18nSelectOptionVO>> getSysDictDataSelectList(String dictType) {
                return ResultVOUtil.emptyData();
            }

            @Override
            public ResultVO<Map<String, Map<Object, Object>>> getSysDictDataMap(List<String> dictType) {
                return ResultVOUtil.emptyData();
            }
        };
    }
}
