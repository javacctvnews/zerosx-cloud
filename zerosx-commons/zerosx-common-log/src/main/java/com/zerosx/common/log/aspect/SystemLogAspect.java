package com.zerosx.common.log.aspect;

import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.IpUtils;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.feign.AsyncSysOperatorLogService;
import com.zerosx.common.log.vo.SystemOperatorLogBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 操作日志处理
 *
 */
@Aspect
@Component
@Slf4j
@ConditionalOnClass({HttpServletRequest.class, RequestContextHolder.class})
public class SystemLogAspect {

    /**
     * 参数及返回值存储最大长度
     */
    public static final Integer MAX_FIELD_LENGTH = 2000;

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    @Autowired
    private AsyncSysOperatorLogService asyncLogService;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(systemLog)")
    public void boBefore(JoinPoint joinPoint, SystemLog systemLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(systemLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, SystemLog systemLog, Object jsonResult) {
        handleLog(joinPoint, systemLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(systemLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, SystemLog systemLog, Exception e) {
        handleLog(joinPoint, systemLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, SystemLog systemLog, final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            SystemOperatorLogBO operLog = new SystemOperatorLogBO();
            operLog.setStatus(0);
            operLog.setOperatorTime(new Date());
            operLog.setOperatorId(ZerosSecurityContextHolder.getOperatorIds());
            // 请求的地址
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            String ip = IpUtils.getRemoteAddr(request);
            operLog.setOperatorIp(ip);
            operLog.setOperatorUrl(StringUtils.substring(request.getRequestURI(), 0, 255));
            String username = ZerosSecurityContextHolder.getUserName();
            if (StringUtils.isNotBlank(username)) {
                operLog.setOperatorName(username);
            }

            if (e != null) {
                operLog.setStatus(1);
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, MAX_FIELD_LENGTH));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethodName(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, systemLog, operLog, jsonResult, request);
            // 设置消耗时间
            operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 保存数据库
            asyncLogService.saveSysLog(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param systemLog 日志
     * @param operLog   操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, SystemLog systemLog, SystemOperatorLogBO operLog, Object jsonResult, HttpServletRequest request) throws Exception {
        // 设置action动作
        operLog.setBusinessType(systemLog.businessType().ordinal());
        // 模块名称
        operLog.setTitle(systemLog.title());
        //模块按钮名称
        operLog.setBtnName(systemLog.btnName());
        // 设置操作人类别
        operLog.setOperatorType(systemLog.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (systemLog.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, systemLog.excludeParamNames(), request);
        }
        // 是否需要保存response，参数和值
        if (systemLog.isSaveRequestData() && jsonResult != null) {
            operLog.setJsonResult(StringUtils.substring(JacksonUtil.toJSONString(jsonResult), 0, MAX_FIELD_LENGTH));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SystemOperatorLogBO operLog, String[] excludeParamNames, HttpServletRequest request) throws Exception {
        String requestMethod = operLog.getRequestMethod();
        Map<?, ?> paramsMap = getParamMap(request);
        if (paramsMap.isEmpty() && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperatorParam(StringUtils.substring(params, 0, MAX_FIELD_LENGTH));
        } else {
            operLog.setOperatorParam(StringUtils.substring(JacksonUtil.toJSONString(paramsMap), 0, MAX_FIELD_LENGTH));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (o != null && !isFilterObject(o)) {
                    try {
                        String jsonObj = JacksonUtil.toJSONString(o);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }

    public static Map<String, String> getParamMap(ServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
            params.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
        }
        return params;
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }
}