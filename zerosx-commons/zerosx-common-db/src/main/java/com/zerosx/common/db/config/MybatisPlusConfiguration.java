package com.zerosx.common.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zerosx.common.db.handler.CustomMetaObjectHandler;
import com.zerosx.common.db.interceptor.CustomTenantInterceptor;
import com.zerosx.common.db.properties.MybatisPlusAutoFillProperties;
import com.zerosx.common.db.properties.TenantProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * mybatis-plus自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(MybatisPlusAutoFillProperties.class)
public class MybatisPlusConfiguration {

    @Autowired
    private TenantLineHandler tenantLineHandler;
    @Autowired
    private TenantProperties tenantProperties;
    @Autowired
    private MybatisPlusAutoFillProperties autoFillProperties;

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        boolean enableTenant = tenantProperties.getEnable();
        //是否开启多租户隔离
        if (enableTenant) {
            CustomTenantInterceptor tenantInterceptor = new CustomTenantInterceptor(tenantLineHandler, tenantProperties.getIgnoreSqls());
            mpInterceptor.addInnerInterceptor(tenantInterceptor);
        }
        //分页插件
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //乐观锁插件
        mpInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //防全表更新与删除插件
        mpInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mpInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "zerosx.mybatis-plus.auto-fill", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler(autoFillProperties);
    }

    /*
    *//**
     * pagehelper的分页插件
     *//*
    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(PageInterceptor pageInterceptor) {
        return configuration -> configuration.addInterceptor(pageInterceptor);
    }*/
}
