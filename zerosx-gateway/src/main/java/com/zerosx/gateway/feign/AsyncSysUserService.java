package com.zerosx.gateway.feign;

import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.ResultVO;
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
    private ISysUserService sysUserService;
    @Lazy
    @Resource
    private IAuthService authService;

    @Async
    public Future<ResultVO<LoginUserTenantsBO>> currentLoginUser(String username) {
        return new AsyncResult<>(sysUserService.currentLoginUser(username));
    }

    @Async
    public Future<ResultVO<OauthClientDetailsBO>> getClient(String clientId) {
        return new AsyncResult<>(authService.getClient(clientId));
    }
}
