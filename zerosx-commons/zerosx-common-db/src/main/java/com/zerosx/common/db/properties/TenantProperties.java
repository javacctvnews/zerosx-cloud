package com.zerosx.common.db.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;


/**
 * @author javacctvnews
 * @description 多租户配置
 * @date Created in 2020/11/10 12:42
 * @modify by
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "zerosx.tenant")
@RefreshScope
public class TenantProperties {

    /**
     * 是否开启多租户
     */
    private Boolean enable = false;

    /**
     * 租户字段
     */
    private String tenantColumn = "operator_id";

    /**
     * 配置不进行多租户隔离的表名
     */
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 配置不进行多租户隔离的sql
     * 需要配置mapper的全路径如：com.zeros.user.mapper.SysUserMapper.findList
     */
    private List<String> ignoreSqls = new ArrayList<>();
}
