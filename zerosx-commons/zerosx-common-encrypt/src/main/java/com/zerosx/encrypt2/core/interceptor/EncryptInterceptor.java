package com.zerosx.encrypt2.core.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.core.enums.EncryptMode;
import com.zerosx.encrypt2.core.properties.EncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * EncryptInterceptor
 * <p>加密拦截器
 *
 * @author: javacctvnews
 * @create: 2023-09-14 10:19
 **/
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class EncryptInterceptor extends AbsEncryptInterceptor {

    public EncryptInterceptor(EncryptProperties encryptProperties) {
        super(encryptProperties);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //未启用
        if (inactiveEncrypt()) {
            return invocation.proceed();
        }
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatements = (MappedStatement) args[0];
        String namespace = getNamespace(mappedStatements);
        debug("执行MappedStatementId【{}】", namespace);
        Object paramObject = args[1];
        if (Objects.isNull(paramObject)) {
            return invocation.proceed();
        }
        Method method = getMethodByNamespace(namespace);
        //情况1：查询参数为单个String且没有注解@param
        if (paramObject instanceof String) {
            EncryptField encryptField = AnnotationUtils.findAnnotation(method, EncryptField.class);
            if (Objects.isNull(encryptField)) {
                return invocation.proceed();
            }
            if (encryptField.mode().equals(EncryptMode.DEC)) {
                return invocation.proceed();
            }
            args[1] = encrypt(encryptField, (String) args[1], EncryptMode.ENC);
            return invocation.proceed();
        }
        // 情况2：如果参数为单个实体对象
        if (!(paramObject instanceof Map)) {
            encryptEntityHandle(args, EncryptMode.ENC);
            return invocation.proceed();
        }
        Map<String, Object> parameterObjectMap = (Map<String, Object>) paramObject;
        // 情况3：查询参数是单个：List<String>、String[]的情况
        EncryptField encryptField = AnnotationUtil.getAnnotation(method, EncryptField.class);
        if (Objects.nonNull(encryptField)) {
            if (encryptField.mode().equals(EncryptMode.ALL) || encryptField.mode().equals(EncryptMode.ENC)) {
                encryptCollectionHandle(encryptField, parameterObjectMap);
                return invocation.proceed();
            }
        }
        Object cloneMap = mapClone(parameterObjectMap);
        if (cloneMap instanceof Map) {
            Map<String, Object> cloneMapTemp = (Map<String, Object>) cloneMap;
            Iterator<Map.Entry<String, Object>> iterator = cloneMapTemp.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof AbstractWrapper || value instanceof Page) {
                    cloneMapTemp.put(key, parameterObjectMap.get(key));
                }
            }
            args[1] = cloneMapTemp;
        } else {
            args[1] = cloneMap;
        }
        Set<EncryptField> encryptFields = getMethodEncryptFields(method);
        encryptParam(cloneMap, EncryptMode.ENC, encryptFields);
        return invocation.proceed();
    }

}
