package com.zerosx.api.system;

import com.zerosx.api.system.factory.SysMenuControllerApiFallbackFactory;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysPermissionBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServiceIdConstants.SYSTEM, contextId = ServiceIdConstants.SYSTEM, fallbackFactory = SysMenuControllerApiFallbackFactory.class)
public interface ISysMenuControllerApi {

    /**
     * 查询角色对应的权限集合
     *
     * @param rolePermissionDTO
     * @return
     */
    @PostMapping("/sys/menu/role_permissions")
    ResultVO<SysPermissionBO> findByRoleCodes(@RequestBody RolePermissionDTO rolePermissionDTO);

}
