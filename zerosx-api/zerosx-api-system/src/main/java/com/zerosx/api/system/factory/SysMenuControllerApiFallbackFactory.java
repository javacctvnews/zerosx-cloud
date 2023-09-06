package com.zerosx.api.system.factory;

import com.zerosx.api.system.ISysMenuControllerApi;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysPermissionBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SysMenuControllerApiFallbackFactory implements FallbackFactory<ISysMenuControllerApi> {

    @Override
    public ISysMenuControllerApi create(Throwable cause) {
        return new ISysMenuControllerApi() {
            @Override
            public ResultVO<SysPermissionBO> findByRoleCodes(RolePermissionDTO rolePermissionDTO) {
                log.error("查询角色权限异常", cause);
                return ResultVOUtil.emptyData();
            }
        };
    }
}
