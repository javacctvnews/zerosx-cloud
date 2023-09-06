package com.zerosx.common.log.feign;

import com.zerosx.common.log.vo.SystemOperatorLogBO;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName AsyncLogService
 * @Description 异步记录日志的service
 * @Author javacctvnews
 * @Date 2023/4/2 14:05
 * @Version 1.0
 */
@Component
public class AsyncSysOperatorLogService {

    @Lazy
    @Resource
    private ISysOperatorLogService systemOperatorLogService;

    @Async
    public void saveSysLog(SystemOperatorLogBO systemOperatorLogBO) {
        systemOperatorLogService.add(systemOperatorLogBO);
    }

}
