package com.zerosx.common.core.config;

import com.zerosx.common.core.config.properties.EasyExcelProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * ExportConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-25 23:50
 **/
@AutoConfiguration
@EnableConfigurationProperties({EasyExcelProperties.class})
public class EasyExcelConfig {


}
