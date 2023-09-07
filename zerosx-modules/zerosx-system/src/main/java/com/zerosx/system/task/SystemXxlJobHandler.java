package com.zerosx.system.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerosx.system.service.ISystemOperatorLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * XxlJobHandler
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-07 13:39
 **/
@Slf4j
@Component
public class SystemXxlJobHandler {

    private static final int LOG_SAVE = 31;

    @Autowired
    private ISystemOperatorLogService systemOperatorLogService;

    /**
     * 自动删除31天前的操作日志
     */
    @XxlJob(value = "deleteSysOpLog")
    public void deleteSysOpLog() {
        int days = LOG_SAVE;
        String jobParam = XxlJobHelper.getJobParam();
        if (StringUtils.isNotBlank(jobParam)) {
            try {
                days = Integer.parseInt(jobParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        boolean deleted = systemOperatorLogService.deleteSystemOperatorLog(days);
        log.debug("删除{}天前的日志：{}", days, deleted);
    }

}
