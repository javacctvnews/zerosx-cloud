package com.zerosx.encrypt2.core.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.anno.EncryptFields;
import com.zerosx.encrypt2.core.EncryptFactory;
import com.zerosx.encrypt2.core.encryptor.IEncryptor;
import com.zerosx.encrypt2.core.enums.EncryptMode;
import com.zerosx.encrypt2.core.properties.EncryptProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AbsEncryptInterceptor
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-14 10:19
 **/
@Slf4j
public abstract class AbsEncryptInterceptor implements Interceptor {

    protected final EncryptProperties encryptProperties;

    public AbsEncryptInterceptor(EncryptProperties encryptProperties) {
        this.encryptProperties = encryptProperties;
    }

    private static final String EMPTY = StringUtils.EMPTY;

    private static final String SYNTAX = "(?:=|!=|LIKE|NOT LIKE|IN|NOT IN|>|<|<=|>=|<>)";

    private static final String PATTERN = "(\\w+\\s*" + SYNTAX + "\\s*[\\w\\S]*)(\\#\\{\\w+\\})";

    private static final Pattern COMPILE_PATTERN;

    private static final Pattern COMPILE_SYNTAX;

    static {
        COMPILE_PATTERN = Pattern.compile(PATTERN);
        COMPILE_SYNTAX = Pattern.compile(SYNTAX);
    }

    protected static AsyncCache<String, Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)//过期时间
            .maximumSize(1000)//最大条数1000
            .buildAsync();//定义cache

    protected void debug(String s, Object... objects) {
        if (encryptProperties.getDebug()) {
            log.debug(s, objects);
        }
    }

    /**
     * 是否激活
     *
     * @return
     */
    protected boolean inactiveEncrypt() {
        if (encryptProperties == null || !encryptProperties.getEnabled()) {
            log.warn("mybatis数据安全加解密未激活！");
            return true;
        }
        return false;
    }

    /**
     * 创建指定算法的IEncryptor实例
     *
     * @param encryptField
     * @return
     */
    protected IEncryptor getEncryptIfAbsent(EncryptField encryptField) {
        String password = StringUtils.isBlank(encryptField.password()) ? encryptProperties.getPassword() : encryptField.password();
        return EncryptFactory.getEncryptIfAbsent(encryptField.algo(), password);
    }

    /**
     * 加密字段
     *
     * @param encryptField
     * @param value
     * @return
     */
    protected String encrypt(EncryptField encryptField, String value, EncryptMode encryptMode) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        IEncryptor encryptor = getEncryptIfAbsent(encryptField);
        if (encryptor == null) {
            return value;
        }
        String cacheValue = (String) getCacheValue(value);
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        }
        if (EncryptMode.ENC.equals(encryptMode)) {
            String encrypted = encryptor.encrypt(value);
            cache.put(value, CompletableFuture.completedFuture(encrypted));
            debug("加密前:{} 加密后:{}", value, encrypted);
            return encrypted;
        } else if (EncryptMode.DEC.equals(encryptMode)) {
            String decrypted = encryptor.decrypt(value);
            cache.put(value, CompletableFuture.completedFuture(decrypted));
            debug("解密前:{} 解密后:{}", value, decrypted);
            return decrypted;
        }
        return value;
    }

    @SneakyThrows
    private static Object getCacheValue(String key) {
        CompletableFuture<Object> completableFuture = cache.getIfPresent(key);
        if (completableFuture == null) {
            //log.debug("【获取】Caffeine缓存, {} = 缓存为空", key);
            return null;
        }
        Object cacheValue = completableFuture.get();
        //log.debug("【获取】Caffeine缓存 {} = {}", key, cacheValue);
        return cacheValue;
    }

    /**
     * 命名空间获取
     */
    public String getNamespace(MappedStatement mappedStatements) {
        return mappedStatements.getId().endsWith("_COUNT") ? StrUtil.removeSuffix(mappedStatements.getId(), "_COUNT") : mappedStatements.getId();
    }

    /**
     * 根据命名空间获取Method对象
     */
    public Method getMethodByNamespace(String namespace) throws ClassNotFoundException {
        String classPath = namespace.substring(0, namespace.lastIndexOf("."));
        String methodName = namespace.substring(namespace.lastIndexOf(".") + 1);
        Class<?> clazz = Class.forName(classPath);
        return ReflectUtil.getMethodByName(clazz, methodName);
    }

    /**
     * 获取MappedStatement对象
     */
    protected MappedStatement getMappedStatement(DefaultResultSetHandler resultSetHandler) throws NoSuchFieldException, IllegalAccessException {
        Class<DefaultResultSetHandler> handlerClass = DefaultResultSetHandler.class;
        Field mappedStatementFiled = handlerClass.getDeclaredField("mappedStatement");
        mappedStatementFiled.setAccessible(true);
        return (MappedStatement) mappedStatementFiled.get(resultSetHandler);
    }

    /**
     * 获取method的EncryptField注解
     *
     * @param method
     * @return
     */
    protected Set<EncryptField> getMethodEncryptFields(Method method) {
        EncryptFields encryptFields = AnnotationUtil.getAnnotation(method, EncryptFields.class);
        if (encryptFields != null) {
            return Arrays.stream(encryptFields.value()).collect(Collectors.toSet());
        }
        EncryptField encryptField = AnnotationUtil.getAnnotation(method, EncryptField.class);
        if (encryptField != null) {
            return new HashSet<>(Collections.singletonList(encryptField));
        }
        return new HashSet<>();
    }

    /**
     * 获取字段的@EncryptField
     *
     * @param field
     * @return
     */
    protected Set<EncryptField> getFieldEncryptFields(Field field) {
        EncryptFields encryptFields = field.getAnnotation(EncryptFields.class);
        if (Objects.nonNull(encryptFields)) {
            return Arrays.stream(encryptFields.value()).collect(Collectors.toSet());
        }
        EncryptField encryptField = AnnotationUtil.getAnnotation(field, EncryptField.class);
        if (encryptField != null) {
            return Stream.of(encryptField).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    /**
     * map对象克隆
     *
     * @param map 需要克隆map
     * @return 克隆后map
     */
    public Object mapClone(Map<String, Object> map) {
        Map<String, Object> cloneMap = new HashMap<>(map);
        // 重新克隆入参，防止在后续的业务逻辑中继续使用加密数据从而造成重复加密
        return objectClone(cloneMap);
    }

    /**
     * 对象深拷贝
     *
     * @param obj 克隆对象
     * @return 克隆后对象
     */
    public Object objectClone(Object obj) {
        Object cloneObj;
        try {
            //无需手动关闭
            ByteOutputStream bos = new ByteOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            //无需手动关闭
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            cloneObj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
            //throw new RuntimeException("请求参数克隆失败请将对象【" + obj.getClass().getName() + "】实现Serializable接口");
        }
        return cloneObj;
    }

    /**
     * 判断集合中是否含有相同地址的对象
     */
    public boolean isEqualityListItem(List<Object> alreadyList, Object obj) {
        for (Object item : alreadyList) {
            if (item == obj) {
                return true;
            }
        }
        return false;
    }

    /**
     * 入参为单个 List<String>、String[]入参加密处理
     *
     * @param encryptField
     * @param parameterObjectMap
     */
    public void encryptCollectionHandle(EncryptField encryptField, Map<String, Object> parameterObjectMap) {
        Object mapValue = null;
        for (Map.Entry<String, Object> mapEntity : parameterObjectMap.entrySet()) {
            Object mapEntityValue = mapEntity.getValue();
            //是否List<String>、String[]
            if (!(mapEntityValue instanceof Collection || mapEntityValue.getClass().isArray() || mapEntity instanceof Map)) {
                debug("非集合类型:{}", mapEntityValue.getClass().getName());
                return;
            }
            if (Objects.isNull(mapValue)) {
                mapValue = mapEntityValue;
                continue;
            }
            if (mapValue != mapEntityValue) {
                return;
            }
        }
        IEncryptor encryptor = getEncryptIfAbsent(encryptField);
        if (mapValue instanceof Collection) {
            Collection<Object> coll = (Collection<Object>) mapValue;
            if (CollUtil.isEmpty(coll)) {
                return;
            }
            Object first = CollUtil.getFirst(coll);
            if (Objects.isNull(first) || !(first instanceof String)) {
                return;
            }
            Collection<String> encryptColl = CollUtil.create(String.class);
            for (Object item : coll) {
                encryptColl.add(encryptor.encrypt((String) item));
            }
            parameterObjectMap.replaceAll((k, v) -> encryptColl);
        } else {
            Object[] arr = (Object[]) mapValue;
            if (ArrayUtil.isEmpty(arr)) {
                return;
            }
            if (!(arr[0] instanceof String)) {
                return;
            }
            Object[] encryptArr = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                encryptArr[i] = encryptor.encrypt((String) arr[i]);
            }
            parameterObjectMap.replaceAll((k, v) -> encryptArr);
        }
    }


    /**
     * 单个String入参加密处理
     *
     * @param args 参数
     */
    public void encryptEntityHandle(Object[] args, EncryptMode encryptMode) {
        Object paramObject = args[1];
        if (paramObject instanceof Long) {
            return;
        }
        Class<?> paramObjectClass = paramObject.getClass();
        if (!EncryptFactory.containsEncrypt(paramObjectClass)) {
            return;
        }
        encryptObject(paramObject, encryptMode);
        //不拷贝对象，会丢失自增ID等一些字段信息
        //Object cloneObj = objectClone(paramObject);
        //encryptObject(cloneObj, encryptMode);
        //args[1] = cloneObj;
    }

    /**
     * 加密对象
     *
     * @param paramObject
     */
    @SneakyThrows
    protected void encryptObject(Object paramObject, EncryptMode encryptMode) {
        //获取需要加解密的字段
        Set<Field> fields = EncryptFactory.getEncryptFields(paramObject.getClass());
        encryptObject(paramObject, encryptMode, fields);
    }

    protected void encryptObject(Object paramObject, EncryptMode encryptMode, Set<Field> fields) throws IllegalAccessException {
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        for (Field field : fields) {
            Object object = field.get(paramObject);
            if (Objects.isNull(object)) {
                continue;
            }
            EncryptField encryptField = field.getAnnotation(EncryptField.class);
            if (encryptField != null) {
                if (object instanceof String) {
                    //加密并设值
                    field.set(paramObject, encrypt(encryptField, (String) object, encryptMode));
                } else if (object instanceof Collection<?>) {//集合类型
                    Collection<Object> collection = (Collection<Object>) object;
                    if (CollectionUtils.isNotEmpty(collection)) {
                        Object firstObj = CollUtil.getFirst(collection.iterator());
                        if (firstObj instanceof String) {
                            Collection<Object> newColl = CollUtil.create(String.class);
                            for (Object item : collection) {
                                newColl.add(encrypt(encryptField, (String) item, encryptMode));
                            }
                            field.set(paramObject, newColl);
                        } else {
                            encryptObject(firstObj, encryptMode);
                        }
                    }
                } else if (object.getClass().isArray()) { // 实体中包含集合
                    Object[] array = (Object[]) object;
                    if (ArrayUtil.isNotEmpty(array)) {
                        if (array[0] instanceof String) {// 集合泛型为String
                            for (int i = 0; i < array.length; i++) {
                                String item = (String) array[i];
                                array[i] = encrypt(encryptField, item, encryptMode);
                            }
                        } else {
                            encryptObject(array[0], encryptMode);
                        }
                    }
                }
            } else {
                Set<EncryptField> encryptFields = getFieldEncryptFields(field);
                if (CollectionUtils.isEmpty(encryptFields)) {
                    continue;
                }
                Map<String, Object> objectMap = (Map<String, Object>) object;
                for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        encryptFields.stream().filter(e -> e.field().equals(entry.getKey())).findFirst()
                                .ifPresent(ef -> objectMap.put(entry.getKey(), encrypt(ef, (String) value, encryptMode)));
                    }
                }
            }
        }
    }

    @SneakyThrows
    public void encryptParam(Object cloneMap, EncryptMode encryptMode, Set<EncryptField> encryptFields) {
        List<Object> alreadyList = new ArrayList<>();
        if (cloneMap instanceof Map) {
            debug("Map类型实例-----------------");
            Map<String, Object> map = ((Map<String, Object>) cloneMap);
            for (Map.Entry<String, Object> mapEntity : map.entrySet()) {
                Object mapEntityValue = mapEntity.getValue();
                if (Objects.isNull(mapEntityValue)) {
                    continue;
                }
                if (isEqualityListItem(alreadyList, mapEntityValue)) {
                    continue;
                }
                alreadyList.add(mapEntityValue);
                if (mapEntityValue instanceof String) {
                    String key = mapEntity.getKey();
                    encryptFields.stream().filter(e -> e.field().equals(key)).findFirst()
                            .ifPresent(encryptField -> map.put(mapEntity.getKey(), encrypt(encryptField, (String) mapEntityValue, encryptMode)));
                } else {
                    encryptParam(mapEntityValue, encryptMode, encryptFields);
                }
            }
        } else if (cloneMap instanceof Collection) {
            debug("Collection类型实例-----------------");
            Collection<?> list = (Collection<?>) cloneMap;
            for (Object item : list) {
                encryptParam(item, encryptMode, encryptFields);
            }
        } else if (cloneMap instanceof AbstractWrapper) {
            AbstractWrapper wrapper = (AbstractWrapper) cloneMap;
            Class<?> modelClazz = wrapper.getEntityClass();
            if (!EncryptFactory.containsEncrypt(modelClazz)) {
                return;
            }
            debug("AbstractWrapper类型实例:{}", modelClazz.getName());
            String sqlSegment = wrapper.getExpression().getSqlSegment();
            Map<String, Object> valuePairs = wrapper.getParamNameValuePairs();

            Field paramNameValuePairs = AbstractWrapper.class.getDeclaredField("paramNameValuePairs");
            String regex = wrapper.getParamAlias() + "." + paramNameValuePairs.getName() + ".";
            sqlSegment = sqlSegment.replaceAll(regex, "");
            if (StringUtils.isBlank(sqlSegment)) {
                return;
            }
            Map<String, String> map = resolve(sqlSegment);
            if (!map.isEmpty()) {
                for (Map.Entry<String, String> e : map.entrySet()) {
                    Object value = valuePairs.get(e.getKey());
                    if (value != null) {
                        EncryptField encryptField = ReflectUtil.getField(modelClazz, StrUtil.toCamelCase(e.getValue())).getAnnotation(EncryptField.class);
                        if (Objects.nonNull(encryptField)) {
                            if (value instanceof String) {
                                String strValue = (String) value;
                                String strValueTemp = strValue.replaceAll("%", "");
                                valuePairs.put(e.getKey(), strValue.replace(strValueTemp, encrypt(encryptField, strValueTemp, encryptMode)));
                            }
                        }
                    }
                }
            }
        } else {
            debug("其他类型:{}", cloneMap.getClass());
            if (!EncryptFactory.containsEncrypt(cloneMap.getClass())) {
                return;
            }
            encryptObject(cloneMap, encryptMode);
        }
    }

    private Map<String, String> resolve(String line) {
        debug("where条件：{}", line);
        Object cacheObject = getCacheValue(line);
        if (Objects.nonNull(cacheObject)) {
            Map<String, String> cacheValue = (Map<String, String>) cacheObject;
            if (MapUtils.isNotEmpty(cacheValue)) {
                return cacheValue;
            }
        }
        Map<String, String> map = new HashMap<>();
        //String line = "(mobile = #{MPGENVAL1} AND status LIKE #{MPGENVAL2} AND id IN (#{MPGENVAL3},#{MPGENVAL4},#{MPGENVAL5}) AND ((id = #{MPGENVAL6}))) GROUP BY id HAVING id > #{MPGENVAL7} ORDER BY add_blacklist_time DESC";
        Matcher m = COMPILE_PATTERN.matcher(line);
        while (m.find()) {
            String syntaxLine = m.group(0).replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("#", "").replaceAll("\\{", "").replaceAll("\\}", "");
            Matcher m2 = COMPILE_SYNTAX.matcher(syntaxLine);
            if (m2.find()) {
                String[] syntaxArray = syntaxLine.split(m2.group(0));
                Arrays.stream(syntaxArray[1].split(",")).forEach(v -> map.put(v.trim(), syntaxArray[0].trim()));
            }
        }
        cache.put(line, CompletableFuture.completedFuture(map));
        return map;
    }

}
