package com.zerosx.common.encrypt.aop;

import cn.hutool.core.util.ReflectUtil;
import com.zerosx.common.encrypt.anno.EncryptField;
import com.zerosx.common.encrypt.core.EncryptorCacheManager;
import com.zerosx.common.encrypt.core.ICustomEncryptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class EncryptParamAspect {

    @Autowired
    private EncryptorCacheManager encryptorCacheManager;

    @Around("execution(* com.zerosx.*.mapper..*(..))")
    public Object pointAround(ProceedingJoinPoint jp) throws Throwable {
        Object target = jp.getTarget();
        Object[] args = jp.getArgs();//得到参数
        String name = jp.getSignature().getName();
        Class<?> declaringType = jp.getSignature().getDeclaringType();
        Method method = ReflectUtil.getMethodByName(declaringType, name);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            EncryptField encryptField = parameter.getAnnotation(EncryptField.class);
            if (encryptField != null) {
                Class<? extends ICustomEncryptor> algo = encryptField.algo();
                Object value = args[i];
                if (value instanceof String) {
                    args[i] = encryptorCacheManager.getEncryptor(algo).encrypt((String) value);
                }
            }
        }
        return jp.proceed(args);
    }

}
