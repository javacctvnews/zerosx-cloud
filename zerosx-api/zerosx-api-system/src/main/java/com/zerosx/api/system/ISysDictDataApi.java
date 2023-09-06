package com.zerosx.api.system;

import com.zerosx.api.system.factory.SysDictDataFallbackFactory;
import com.zerosx.api.system.vo.I18nSelectOptionVO;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


@FeignClient(name = ServiceIdConstants.SYSTEM, contextId = ServiceIdConstants.SYSTEM, fallbackFactory = SysDictDataFallbackFactory.class)
public interface ISysDictDataApi {

    /**
     * 根据字典类型显示下拉框字典数据
     *
     * @param dictType
     * @return
     */
    @GetMapping(value = "/sysDictData_selectList/{dictType}")
    ResultVO<List<I18nSelectOptionVO>> getSysDictDataSelectList(@PathVariable("dictType") String dictType);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType
     * @return
     */
    @PostMapping(value = "/sysDictData_getMap")
    ResultVO<Map<String, Map<Object, Object>>> getSysDictDataMap(@RequestBody List<String> dictType);

}
