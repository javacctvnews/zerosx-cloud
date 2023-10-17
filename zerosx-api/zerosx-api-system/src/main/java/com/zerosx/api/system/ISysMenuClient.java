package com.zerosx.api.system;

import com.zerosx.api.system.factory.SysMenuClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysPermissionBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "ISysMenuClient", name = ServiceIdConstants.SYSTEM, fallbackFactory = SysMenuClientFallback.class)
public interface ISysMenuClient {

    /**
     * 查询角色对应的权限集合
     *
     * @param rolePermissionDTO
     * @return
     */
    @PostMapping("/sys_menu/query_perms")
    ResultVO<SysPermissionBO> queryPermsByRoleIds(@RequestBody RolePermissionDTO rolePermissionDTO);

}
