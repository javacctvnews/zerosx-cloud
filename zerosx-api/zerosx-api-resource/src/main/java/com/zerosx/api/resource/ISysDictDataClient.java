package com.zerosx.api.resource;

import com.zerosx.api.resource.factory.SysDictDataFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "ISysDictDataClient", name = ServiceIdConstants.RESOURCE, fallbackFactory = SysDictDataFallback.class)
public interface ISysDictDataClient {

    /**
     * 查询字典类型的字典数据
     *
     * @param dictType
     * @return
     */
    @GetMapping(value = "/sysDictData/{dictType}/type")
    ResultVO<Map<Object, String>> getDictData(@PathVariable("dictType") String dictType);

    /**
     * 查询字典类型的字典数据
     * @param dictType
     * @return
     */
    @GetMapping(value = "/sysDictData_selectList/{dictType}")
    ResultVO<List<I18nSelectOptionVO>> getDictList(@PathVariable("dictType") String dictType);

}
