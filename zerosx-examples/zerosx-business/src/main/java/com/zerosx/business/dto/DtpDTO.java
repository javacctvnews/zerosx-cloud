package com.zerosx.business.dto;

import lombok.Data;

/**
 * DtpDTO
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-24 22:54
 **/
@Data
public class DtpDTO {

    //线程池名称
    private String threadName;
    //任务数量
    private Integer taskNum;
    //任务提交时间间隔 毫秒
    private Long submitIntervalTime;
    //任务执行时间 毫秒
    private Long executorTime;

}
