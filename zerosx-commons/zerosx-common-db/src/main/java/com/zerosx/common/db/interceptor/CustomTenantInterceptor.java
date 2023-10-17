package com.zerosx.common.db.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.zerosx.common.core.enums.system.UserTypeEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * MyBatis-plus租户拦截器
 */
@Slf4j
public class CustomTenantInterceptor extends TenantLineInnerInterceptor {

    private final List<String> ignoreSqls;

    public CustomTenantInterceptor(TenantLineHandler tenantLineHandler, List<String> ignoreSqls) {
        super(tenantLineHandler);
        this.ignoreSqls = ignoreSqls;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds
            , ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (isIgnoreMappedStatement(ms.getId())) {
            return;
        }
        //超级管理员跳过
        String userType = ZerosSecurityContextHolder.getUserType();
        if(UserTypeEnum.SUPER_ADMIN.getCode().equals(userType)){
            //log.debug("查询语句【{}】跳过------------------", userType);
            return;
        }
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    private boolean isIgnoreMappedStatement(String msId) {
        return ignoreSqls.stream().anyMatch((e) -> e.equalsIgnoreCase(msId));
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        //非select语句跳过
        if (sct != SqlCommandType.SELECT){
            //log.debug("非查询语句直接跳过------------------");
            return;
        }
        super.beforePrepare(sh, connection, transactionTimeout);
    }
}
