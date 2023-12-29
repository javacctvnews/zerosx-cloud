package com.zerosx.business.controller;

import com.zerosx.api.examples.dto.BusinessDTO;
import com.zerosx.business.dto.DtpDTO;
import com.zerosx.business.service.IBusinessService;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.dynamictp.ZExecutor;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class BusinessController {

    @Autowired
    private IBusinessService businessService;
//    @Autowired
//    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 模拟用户购买商品下单业务逻辑流程
     */
    @Operation(summary = "Seata分布式事务")
    @PostMapping("/buy")
    public ResultVO<?> handleBusiness(@RequestBody BusinessDTO businessDTO) {
        log.info("请求参数：{}", businessDTO.toString());
        return ResultVOUtil.success(businessService.handleBusiness(businessDTO));
    }

    @Operation(summary = "DynamicTp动态线程池-测试")
    @PostMapping(value = "/dynamicTp")
    public ResultVO<?> dynamicTp01(@RequestBody DtpDTO dtpDTO) throws InterruptedException {
        Executor executor = ZExecutor.getExecutor(dtpDTO.getThreadName());
        Integer taskNum = dtpDTO.getTaskNum();
        for (int i = 1; i <= taskNum; i++) {
            int a = i;
            Thread.sleep(dtpDTO.getSubmitIntervalTime());
            executor.execute(() -> {
                try {
                    Thread.sleep(dtpDTO.getExecutorTime());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("第{}个分布式ID:{}", a, IdGenerator.nextSid());
            });
        }
        return ResultVOUtil.success();
    }

    @Operation(summary = "动态线程池(ThreadPoolTaskExecutor)")
    @GetMapping(value = "/dynamicTp01/{taskNum}")
    public ResultVO<?> dynamicTp01(@PathVariable("taskNum") Integer taskNum) {
        /*for (int i = 1; i <= taskNum; i++) {
            int a = i;
            threadPoolTaskExecutor.execute(() -> {
                log.debug("第{}个分布式ID:{}", a, IdGenerator.nextSid());
            });
        }*/
        return ResultVOUtil.success();
    }

    @Operation(summary = "动态线程池(ScheduledExecutorService)")
    @PostMapping(value = "/dynamicTp02")
    public ResultVO<?> dynamicTp02(@RequestBody DtpDTO dtpDTO) throws InterruptedException {
        ScheduledExecutorService scheduledExecutor = ZExecutor.getScheduledExecutor(dtpDTO.getThreadName());
        Integer taskNum = dtpDTO.getTaskNum();
        for (int i = 1; i <= taskNum; i++) {
            int a = i;
            Thread.sleep(dtpDTO.getSubmitIntervalTime());
            scheduledExecutor.schedule(() -> {
                try {
                    Thread.sleep(dtpDTO.getExecutorTime());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("第{}个分布式ID:{}", a, IdGenerator.nextSid());
            }, 3, TimeUnit.SECONDS);
        }
        return ResultVOUtil.success();
    }

}
