package com.zerosx.gateway.feign;

import com.zerosx.api.auth.IAccessTokenClient;
import com.zerosx.api.system.ISysMenuClient;
import com.zerosx.api.system.ISysUserClient;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysPermissionBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * @ClassName AsyncSysUserService
 * @Description 异步调用
 * @Author javacctvnews
 * @Date 2023/3/14 16:09
 * @Version 1.0
 */
@Component
@Slf4j
public class AsyncSysUserService {

    @Lazy
    @Resource
    private ISysUserClient sysUserClient;
    @Lazy
    @Resource
    private IAccessTokenClient accessTokenClient;
    @Lazy
    @Resource
    private ISysMenuClient sysMenuClient;

    @Async
    public Future<ResultVO<LoginUserTenantsBO>> currentLoginUser(String username) {
        return new AsyncResult<>(sysUserClient.currentLoginUser(username));
    }

    @Async
    public Future<ResultVO<OauthClientDetailsBO>> getClient(String clientId) {
        return new AsyncResult<>(accessTokenClient.getClient(clientId));
    }

    @Async
    public Future<ResultVO<SysPermissionBO>> queryPermsByRoleIds(RolePermissionDTO rolePermissionDTO) {
        return new AsyncResult<>(sysMenuClient.queryPermsByRoleIds(rolePermissionDTO));
    }
}
