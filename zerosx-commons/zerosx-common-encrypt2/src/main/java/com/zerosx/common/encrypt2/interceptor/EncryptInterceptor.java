//package com.zerosx.common.encrypt.interceptor;
//
//import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.zerosx.common.encrypt.anno.EncryptField;
//import com.zerosx.common.encrypt.core.EncryptModel;
//import com.zerosx.common.encrypt.core.EncryptorCacheManager;
//import com.zerosx.common.encrypt.core.ICustomEncryptor;
//import com.zerosx.common.encrypt.enums.EncryptMode;
//import com.zerosx.common.encrypt.properties.CustomEncryptProperties;
//import com.zerosx.common.encrypt.utils.MybatisEncryptUtils;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Plugin;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Properties;
//import java.util.Set;
//
///**
// * 查询参数加密拦截器
// */
////@Intercepts({
////        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
////        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
////        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
////})
//public class EncryptInterceptor implements Interceptor {
//
//    private static final String PAGE_COUNT = "_mpCount";
//
//    private final EncryptorCacheManager encryptorCacheManager;
//
//    private final CustomEncryptProperties customEncryptProperties;
//
//    public EncryptInterceptor(EncryptorCacheManager encryptorCacheManager, CustomEncryptProperties customEncryptProperties) {
//        this.encryptorCacheManager = encryptorCacheManager;
//        this.customEncryptProperties = customEncryptProperties;
//    }
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object[] args = invocation.getArgs();
//        MappedStatement mappedStatements = (MappedStatement) args[0];
//        Object paramObject = args[1];
//        if (Objects.isNull(paramObject)) {
//            return invocation.proceed();
//        }
//        // 如果查询参数为单个String
//        String namespace = MybatisEncryptUtils.getNamespace(mappedStatements);
//        if (paramObject instanceof String) {
//            singleEncryptHandle(namespace, args);
//            return invocation.proceed();
//        }
//        // 如果参数为单个实体对象
//        if (!(paramObject instanceof Map)) {
//            MybatisEncryptUtils.singleEntityEncryptHandle(namespace, args, encryptorCacheManager);
//            return invocation.proceed();
//        }
//        @SuppressWarnings("unchecked")
//        Map<String, Object> parameterObjectMap = (Map<String, Object>) paramObject;
//        //mapper层方法
//        Method method = MybatisEncryptUtils.getMethodByNamespace(namespace);
//        //不存在（非自定义mapper方法）
//        if (Objects.isNull(method)) {
//            Object cloneMap = MybatisEncryptUtils.mapClone(parameterObjectMap, namespace);
//            if (cloneMap instanceof Map) {
//                Map<String, Object> cloneMapTemp = (Map<String, Object>) cloneMap;
//                for (Map.Entry<String, Object> entry : cloneMapTemp.entrySet()) {
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
//                    if (value instanceof AbstractWrapper || value instanceof Page) {
//                        cloneMapTemp.put(key, parameterObjectMap.get(key));
//                    }
//                }
//                args[1] = cloneMapTemp;
//                Set<EncryptModel> models = MybatisEncryptUtils.mappedStatement2MapperCryptoModel(mappedStatements, EncryptMode.ENCRYPT);
//                MybatisEncryptUtils.paramCrypto(cloneMapTemp, models, EncryptMode.ENCRYPT, encryptorCacheManager);
//            } else {
//                args[1] = cloneMap;
//                Set<EncryptModel> models = MybatisEncryptUtils.mappedStatement2MapperCryptoModel(mappedStatements, EncryptMode.ENCRYPT);
//                MybatisEncryptUtils.paramCrypto(cloneMap, models, EncryptMode.ENCRYPT, encryptorCacheManager);
//            }
//            return invocation.proceed();
//        } else {
//            Set<EncryptModel> models = MybatisEncryptUtils.method2MapperCryptoModel(method, EncryptMode.ENCRYPT);
//            if (CollectionUtils.isNotEmpty(models)) {
//                Object cloneMap = MybatisEncryptUtils.mapClone(parameterObjectMap, namespace);
//                args[1] = cloneMap;
//                MybatisEncryptUtils.paramCrypto(cloneMap, models, EncryptMode.ENCRYPT, encryptorCacheManager);
//                return invocation.proceed();
//            }
//        }
//        return invocation.proceed();
//    }
//
//    /**
//     * 单个String入参加密处理
//     *
//     * @param namespace 方法命名空间
//     * @param args      参数
//     */
//    private void singleEncryptHandle(String namespace, Object[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//        Method method = MybatisEncryptUtils.getMethodByNamespace(namespace);
//        EncryptField cryptoString = AnnotationUtils.findAnnotation(method, EncryptField.class);
//        if (Objects.isNull(cryptoString)) {
//            return;
//        }
//        if (cryptoString.mode().equals(EncryptMode.DECRYPT)) {
//            return;
//        }
//        ICustomEncryptor cryptoRule = cryptoString.rule().newInstance();
//        args[1] = cryptoRule.encrypt((String) args[1]);
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//
//
//}
