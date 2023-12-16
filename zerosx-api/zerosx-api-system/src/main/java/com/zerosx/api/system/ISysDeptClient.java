package com.zerosx.api.system;

import com.zerosx.api.system.factory.SysDeptClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "ISysDeptClient", name = ServiceIdConstants.SYSTEM, fallbackFactory = SysDeptClientFallback.class)
public interface ISysDeptClient {

    /**
     * 查询部门名称
     *
     * @param id 部门id
     * @return 部门名称
     */
    @GetMapping("/sys_dept/queryName/{id}")
    ResultVO<String> queryName(@PathVariable("id") Long id);

}
