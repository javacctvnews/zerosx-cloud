package com.zerosx.common.db.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.db.properties.TenantProperties;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 多租户自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(TenantProperties.class)
public class TenantAutoConfiguration {

    @Autowired
    private TenantProperties tenantProperties;

    @Bean
    public TenantLineHandler tenantLineHandler() {
        return new TenantLineHandler() {

            @Override
            public String getTenantIdColumn() {
                return tenantProperties.getTenantColumn();
            }

            /**
             * 目前支持到一个租户的过滤，后续再扩展多租户及多子租户过滤
             */
            @Override
            public Expression getTenantId() {
                String operatorIds = ZerosSecurityContextHolder.getOperatorIds();
                if (operatorIds != null) {
                    return new StringValue(operatorIds);
                }
                return new NullValue();
            }

            /**
             * 过滤不需要根据租户隔离的表
             * @param tableName 表名
             */
            @Override
            public boolean ignoreTable(String tableName) {
                return tenantProperties.getIgnoreTables().stream().anyMatch(
                        (e) -> e.equalsIgnoreCase(tableName)
                );
            }
        };
    }
}
