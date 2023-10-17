package com.zerosx.api.system.factory;

import com.zerosx.api.system.ISysMenuClient;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysPermissionBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SysMenuClientFallback implements FallbackFactory<ISysMenuClient> {

    @Override
    public ISysMenuClient create(Throwable cause) {
        return new ISysMenuClient() {
            @Override
            public ResultVO<SysPermissionBO> queryPermsByRoleIds(RolePermissionDTO rolePermissionDTO) {
                return ResultVOUtil.emptyData();
            }
        };
    }
}
