package com.zerosx.common.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * EasyExcelProperties
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-25 23:54
 **/
@Data
@RefreshScope
@ConfigurationProperties(prefix = "zerosx.export")
public class EasyExcelProperties {

    /**
     * 是否开启导入导出
     */
    private Boolean enabled = true;

    /**
     * Excel导出每次查询数据库最大记录条数
     */
    private Integer querySize = 50 * 1000;

    /**
     * excel导出每个sheet页最大记录数
     */
    private Integer sheetNum = 1000 * 1000;

}
