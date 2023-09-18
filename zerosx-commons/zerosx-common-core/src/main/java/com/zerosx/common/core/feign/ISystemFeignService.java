package com.zerosx.common.core.feign;

import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @ClassName IOperatorFeignService
 * @Description
 * @Author javacctvnews
 * @Date 2023/5/25 21:57
 * @Version 1.0
 */
@FeignClient(name = ServiceIdConstants.SYSTEM, fallbackFactory = SystemFeignServiceFallback.class)
public interface ISystemFeignService {

    /**
     * 按租户标识查询全称
     *
     * @param operatorId
     * @return
     */
    @PostMapping("/muti_tenancy/tenantName")
    ResultVO<String> transIdName(@RequestParam(value = "operatorId", required = false) String operatorId);


    /**
     * 查询字典类型的字典数据
     *
     * @param dictType
     * @return
     */
    @GetMapping(value = "/sysDictData/{dictType}/type")
    ResultVO<Map<String, String>> getDictData(@PathVariable("dictType") String dictType);


    /**
     * 文件URL获取(单个)
     *
     * @param objectName
     * @return
     */
    @PostMapping(value = "/view_url")
    ResultVO<String> getObjectViewUrl(@RequestParam("objectName") String objectName);

    /**
     * 按行政区域码获取名称
     *
     * @param areaCode
     * @return
     */
    @PostMapping("/area_name")
    ResultVO<String> getAreaName(@RequestParam("areaCode") String areaCode);

}
