package com.zerosx.encrypt2.core.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.core.EncryptFactory;
import com.zerosx.encrypt2.core.enums.EncryptMode;
import com.zerosx.encrypt2.core.properties.EncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * DecryptInterceptor
 * <p>解密拦截器
 *
 * @author: javacctvnews
 * @create: 2023-09-14 17:12
 **/
@Slf4j
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class DecryptInterceptor extends AbsEncryptInterceptor {

    public DecryptInterceptor(EncryptProperties encryptProperties) {
        super(encryptProperties);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //未启用
        if (inactiveEncrypt()) {
            return invocation.proceed();
        }
        //取出查询的结果
        Object resultObject = invocation.proceed();
        if (Objects.isNull(resultObject)) {
            return null;
        }
        if (resultObject instanceof Collection) {
            Collection<Object> coll = (Collection<Object>) resultObject;
            if (CollectionUtils.isEmpty(coll)) {
                return coll;
            }
            Object obj = CollUtil.getFirst(coll);
            if (Objects.isNull(obj)) {
                return resultObject;
            }
            boolean containsEncrypt = EncryptFactory.containsEncrypt(obj.getClass());
            if (containsEncrypt) {
                Set<Field> fields = EncryptFactory.getEncryptFields(obj.getClass());
                for (Object object : coll) {
                    encryptObject(object, EncryptMode.DEC, fields);
                }
            } else if (obj instanceof Map) { // 泛型为Map
                //log.debug("集合的Map类型");
                MappedStatement mappedStatement = getMappedStatement((DefaultResultSetHandler) invocation.getTarget());
                String namespace = getNamespace(mappedStatement);
                Method method = getMethodByNamespace(namespace);
                Set<EncryptField> encryptFieldList = getMethodEncryptFields(method);
                if (CollectionUtils.isEmpty(encryptFieldList)) {
                    return resultObject;
                }
                for (Object object : coll) {
                    Map<String, Object> map = (Map<String, Object>) object;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        Object value = entry.getValue();
                        if (Objects.isNull(value)) {
                            continue;
                        }
                        if (value instanceof String) {
                            encryptFieldList.stream().filter(e -> e.field().equals(entry.getKey())).findFirst()
                                    .ifPresent(encryptField -> map.put(entry.getKey(), encrypt(encryptField, (String) value, EncryptMode.DEC)));
                        } else {
                            debug("其他类型:{} key:{} val:{}", value.getClass().getName(), entry.getKey(), value);
                        }
                    }
                }
                return resultObject;
            } else if (obj instanceof String) { // 泛型为String
                //log.debug("集合的String类型");
                MappedStatement mappedStatement = getMappedStatement((DefaultResultSetHandler) invocation.getTarget());
                String namespace = getNamespace(mappedStatement);
                Method method = getMethodByNamespace(namespace);
                EncryptField encryptField = AnnotationUtil.getAnnotation(method, EncryptField.class);
                if (Objects.isNull(encryptField)) {
                    return resultObject;
                }
                if (encryptField.mode().equals(EncryptMode.ENC)) {
                    return resultObject;
                }
                Collection<Object> result = CollUtil.create(String.class);
                for (Object item : coll) {
                    result.add(encrypt(encryptField, (String) item, EncryptMode.DEC));
                }
                return result;
            }
        } else {
            boolean containsEncrypt = EncryptFactory.containsEncrypt(resultObject.getClass());
            if (containsEncrypt) {
                encryptObject(resultObject, EncryptMode.DEC);
            }
        }
        return resultObject;
    }

}
