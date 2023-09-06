package com.zerosx.common.encrypt2.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.zerosx.common.encrypt2.anno.*;
import com.zerosx.common.encrypt2.core.EncryptModel;
import com.zerosx.common.encrypt2.core.EncryptorCacheManager;
import com.zerosx.common.encrypt2.core.ICustomEncryptor;
import com.zerosx.common.encrypt2.enums.EncryptMode;
import org.apache.ibatis.mapping.MappedStatement;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加解密公共组件
 *
 * @author : wdh
 * @since : 2022/5/24 16:25
 */
public class MybatisEncryptUtils {

    private static final String PAGE_COUNT = "_mpCount";

    /**
     * 判断集合中是否含有相同地址的对象
     */
    public static boolean isEqualityListItem(List<Object> alreadyList, Object obj) {
        for (Object item : alreadyList) {
            if (item == obj) {
                return true;
            }
        }
        return false;
    }

    /**
     * 命名空间获取
     * com.zerosx.system.mapper.ISysUserMapper.selectPage_mpCount
     */
    public static String getNamespace(MappedStatement mappedStatements) {
        return mappedStatements.getId().endsWith(PAGE_COUNT) ? StrUtil.removeSuffix(mappedStatements.getId(), PAGE_COUNT) : mappedStatements.getId();
    }

    /**
     * 根据命名空间获取Method对象
     */
    public static Method getMethodByNamespace(String namespace) throws ClassNotFoundException {
        String classPath = namespace.substring(0, namespace.lastIndexOf("."));
        String methodName = namespace.substring(namespace.lastIndexOf(".") + 1);
        Class<?> clazz = Class.forName(classPath);
        return ReflectUtil.getMethodByName(clazz, methodName);
    }

    /**
     * 方法获取CryptoKey注解信息
     */
    public static Set<EncryptModel> method2MapperCryptoModel(Method method, EncryptMode mode) {
        Set<EncryptModel> models = new HashSet<>();
        if (Objects.nonNull(method)) {
            EncryptKeys cryptoKeys = AnnotationUtil.getAnnotation(method, EncryptKeys.class);
            if (Objects.nonNull(cryptoKeys)) {
                setMapperCryptoModel(mode, models, cryptoKeys);
            } else {
                EncryptKey cryptoKey = AnnotationUtil.getAnnotation(method, EncryptKey.class);
                setMapperCryptoModel(mode, models, cryptoKey);
            }
        }
        return models;
    }

    /**
     * 字段获取CryptoKey注解信息
     *
     * @param field 字段
     * @param mode  加解密方式
     * @return MapperCryptoModel
     */
    public static Set<EncryptModel> field2MapperCryptoModel(Field field, EncryptMode mode) {
        Set<EncryptModel> models = CollUtil.newHashSet();

        EncryptKeys cryptoKeys = field.getAnnotation(EncryptKeys.class);
        if (Objects.nonNull(cryptoKeys)) {
            setMapperCryptoModel(mode, models, cryptoKeys);
        } else {
            EncryptKey cryptoKey = field.getAnnotation(EncryptKey.class);
            setMapperCryptoModel(mode, models, cryptoKey);
        }
        return models;
    }

    private static void setMapperCryptoModel(EncryptMode mode, Set<EncryptModel> models, EncryptKeys cryptoKeys) {
        for (EncryptKey cryptoKey : cryptoKeys.value()) {
            if (cryptoKey.mode().equals(EncryptMode.ALL) || cryptoKey.mode().equals(mode)) {
                EncryptModel model = new EncryptModel();
                model.setField(cryptoKey.key());
                model.setRule(cryptoKey.rule());
                models.add(model);
            }
        }
    }

    private static void setMapperCryptoModel(EncryptMode mode, Set<EncryptModel> models, EncryptKey cryptoKey) {
        if (Objects.isNull(cryptoKey)) {
            return;
        }
        if (cryptoKey.mode().equals(EncryptMode.ALL) || cryptoKey.mode().equals(mode)) {
            EncryptModel model = new EncryptModel();
            model.setField(cryptoKey.key());
            model.setRule(cryptoKey.rule());
            models.add(model);
        }
    }

    /**
     * mappedStatement获取注解信息
     *
     * @param mappedStatement mappedStatement
     * @param mode            加解密模式
     */
    public static Set<EncryptModel> mappedStatement2MapperCryptoModel(MappedStatement mappedStatement, EncryptMode mode) throws
            ClassNotFoundException {
        Method method = getMethodByMappedStatement(mappedStatement);
        return method2MapperCryptoModel(method, mode);
    }

    /**
     * 获取Method对象
     */
    public static Method getMethodByMappedStatement(MappedStatement mappedStatement) throws
            ClassNotFoundException {
        String namespace = getNamespace(mappedStatement);
        return getMethodByNamespace(namespace);
    }

    /**
     * 对象深拷贝
     *
     * @param obj       克隆对象
     * @param namespace 对象命名空间
     * @return 克隆后对象
     */
    public static Object objectClone(Object obj, String namespace) {
        Object cloneObj;
        try {
            ByteOutputStream bos = new ByteOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            cloneObj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            throw new RuntimeException(namespace + " 方法请求参数克隆失败！请将对象实现Serializable接口");
        }
        return cloneObj;
    }

    /**
     * 根据方式加解密数据
     *
     * @param str        原数据
     * @param cryptoRule 加解密规则
     * @param mode       方式
     * @return 加解密后数据
     */
    public static String cryptoByMode(String str, ICustomEncryptor cryptoRule, EncryptMode mode) {
        if (EncryptMode.ENCRYPT.equals(mode)) {
            return cryptoRule.encrypt(str);
        } else if (EncryptMode.DECRYPT.equals(mode)) {
            return cryptoRule.decrypt(str);
        } else {
            return str;
        }
    }

    /**
     * 实体对象加密
     *
     * @param paramsObject 实体对象
     * @param mode         加解密模式
     */
    public static <T> T entityCrypto(T paramsObject, EncryptMode mode, EncryptorCacheManager encryptorCacheManager) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<?> paramObjectClass = paramsObject.getClass();
        Field[] declaredFields = paramObjectClass.getDeclaredFields();
        for (Field field : declaredFields) {
            EncryptField cryptoString = field.getAnnotation(EncryptField.class);
            if (Objects.nonNull(cryptoString)) {
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                if (Objects.isNull(object)) {
                    continue;
                }
                if (object instanceof String) {
                    ICustomEncryptor cryptoRule = encryptorCacheManager.getEncryptor(cryptoString.rule());
                    field.set(paramsObject, cryptoByMode((String) object, cryptoRule, mode));
                } else if (object instanceof Collection<?>) { // 实体中包含集合
                    @SuppressWarnings("unchecked")
                    Collection<Object> coll = (Collection<Object>) object;
                    if (CollUtil.isNotEmpty(coll)) {
                        Object firstObj = CollUtil.getFirst(coll.iterator());
                        if (!(coll.size() == 1 && Objects.isNull(firstObj)) && firstObj instanceof String) {// 集合泛型为String
                            ICustomEncryptor cryptoRule = encryptorCacheManager.getEncryptor(cryptoString.rule());
                            Collection<Object> newColl = CollUtil.create(String.class);
                            for (int i = 0; i < coll.size(); i++) {
                                String item = (String) CollUtil.get(coll, i);
                                newColl.add(cryptoByMode(item, cryptoRule, mode));
                            }
                            field.set(paramsObject, newColl);
                        }
                    }
                } else if (object.getClass().isArray()) { // 实体中包含集合
                    Object[] array = (Object[]) object;
                    if (ArrayUtil.isNotEmpty(array)) {
                        if (array[0] instanceof String) {// 集合泛型为String
                            ICustomEncryptor cryptoRule = encryptorCacheManager.getEncryptor(cryptoString.rule());
                            for (int i = 0; i < array.length; i++) {
                                String item = (String) array[i];
                                array[i] = cryptoByMode(item, cryptoRule, mode);
                            }
                        }
                    }
                }
                continue;
            }

            // 处理实体属性是对象的情况
            EncryptClass cryptoClass = field.getAnnotation(EncryptClass.class);
            if (Objects.nonNull(cryptoClass)) {
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                if (Objects.isNull(object)) {
                    continue;
                }
                if (object instanceof Collection) {
                    Collection<?> coll = (Collection<?>) object;
                    for (Object item : coll) {
                        entityCrypto(item, mode, encryptorCacheManager);
                    }
                } else {
                    entityCrypto(object, mode, encryptorCacheManager);
                }
            }

            // 实体中有@CryptoKey注解
            Set<EncryptModel> models = field2MapperCryptoModel(field, EncryptMode.ENCRYPT);
            if (CollUtil.isNotEmpty(models)) {
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                if (Objects.isNull(object)) {
                    continue;
                }
                if (object instanceof Collection<?>) {
                    Collection<?> coll = (Collection<?>) object;
                    if (CollUtil.isNotEmpty(coll)) {
                        Object firstObj = CollUtil.getFirst(coll);
                        if (!(coll.size() == 1 && Objects.isNull(firstObj)) && firstObj instanceof Map) {
                            paramCrypto(object, models, mode, encryptorCacheManager);
                        }
                    }
                } else if (object instanceof Map) {
                    paramCrypto(object, models, mode, encryptorCacheManager);
                }
            }
        }
        return paramsObject;
    }

    /**
     * map对象克隆
     *
     * @param map       需要克隆map
     * @param namespace 方法命名空间
     * @return 克隆后map
     */
    public static Object mapClone(Map<String, Object> map, String namespace) {
        HashMap<String, Object> cloneMap = MapUtil.newHashMap();
        cloneMap.putAll(map);
        // 重新克隆入参，防止在后续的业务逻辑中继续使用加密数据从而造成重复加密
        return MybatisEncryptUtils.objectClone(cloneMap, namespace);
    }

    /**
     * 实体对象外的其它数据加解密
     *
     * @param parameterObject 加解密数据
     * @param models          CryptoKey注解信息
     * @param mode            加解密模式
     */
    public static void paramCrypto(Object parameterObject, Set<EncryptModel> models, EncryptMode mode, EncryptorCacheManager encryptorCacheManager) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<Object> alreadyList = new ArrayList<>();

        if (parameterObject instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = ((Map<String, Object>) parameterObject);
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
                    for (EncryptModel model : models) {
                        if (model.getField().equals(mapEntity.getKey())) {
                            Class<? extends ICustomEncryptor> rule = model.getRule();
                            ICustomEncryptor cryptoRule = rule.newInstance();
                            map.put(model.getField(), cryptoByMode((String) mapEntityValue, cryptoRule, mode));
                            break;
                        }
                    }
                } else {
                    paramCrypto(mapEntityValue, models, mode, encryptorCacheManager);
                }
            }
        } else if (parameterObject instanceof Collection) {
            Collection<?> list = (Collection<?>) parameterObject;
            for (Object item : list) {
                paramCrypto(item, models, mode, encryptorCacheManager);
            }
        } else if (parameterObject instanceof AbstractWrapper) {
            AbstractWrapper wrapper = (AbstractWrapper) parameterObject;
            Class<?> modelClazz = wrapper.getEntityClass();
            String sqlSegment = wrapper.getExpression().getSqlSegment();
            Map<String, Object> valuePairs = wrapper.getParamNameValuePairs();

            Field paramNameValuePairs = AbstractWrapper.class.getDeclaredField("paramNameValuePairs");
            String regex = wrapper.getParamAlias() + "." + paramNameValuePairs.getName() + ".";
            sqlSegment = sqlSegment.replaceAll(regex, "");
            Map<String, String> map = resolve(sqlSegment);
            if (!map.isEmpty()) {
                for (Map.Entry<String, String> e : map.entrySet()) {
                    Object value = valuePairs.get(e.getKey());
                    if (value != null) {
                        EncryptField cryptoString = ReflectUtil.getField(modelClazz, StrUtil.toCamelCase(e.getValue())).getAnnotation(EncryptField.class);
                        if (Objects.nonNull(cryptoString)) {
                            if (value instanceof String) {
                                String strValue = (String) value;
                                String strValueTemp = strValue.replaceAll("%", "");
                                ICustomEncryptor cryptoRule = encryptorCacheManager.getEncryptor(cryptoString.rule());
                                valuePairs.put(e.getKey(), strValue.replace(strValueTemp, cryptoRule.encrypt(strValueTemp)));
                            }
                        }
                    }
                }
            }
        } else {
            EncryptClass cryptoClass = AnnotationUtil.getAnnotation(parameterObject.getClass(), EncryptClass.class);
            if (Objects.nonNull(cryptoClass)) {
                entityCrypto(parameterObject, mode, encryptorCacheManager);
            }
        }
    }

    private static Map<String, String> resolve(String line) {
        Map<String, String> map = new HashMap<>();
        //String line = "(mobile = #{MPGENVAL1} AND status LIKE #{MPGENVAL2} AND id IN (#{MPGENVAL3},#{MPGENVAL4},#{MPGENVAL5}) AND ((id = #{MPGENVAL6}))) GROUP BY id HAVING id > #{MPGENVAL7} ORDER BY add_blacklist_time DESC";
        String syntax = "(?:=|!=|LIKE|NOT LIKE|IN|NOT IN|>|<|<=|>=|<>)";
        String pattern = "(\\w+\\s*" + syntax + "\\s*[\\w\\S]*)(\\#\\{\\w+\\})";

        Matcher m = Pattern.compile(pattern).matcher(line);
        while (m.find()) {
            String syntaxLine = m.group(0).replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("#", "").replaceAll("\\{", "").replaceAll("\\}", "");
            Matcher m2 = Pattern.compile(syntax).matcher(syntaxLine);
            if (m2.find()) {
                String[] syntaxArray = syntaxLine.split(m2.group(0));
                Arrays.stream(syntaxArray[1].split(",")).forEach(v -> map.put(v.trim(), syntaxArray[0].trim()));
            }
        }
        return map;
    }

    /**
     * 字段加密
     *
     * @param cryptoString 注解
     * @param val          加密字段值
     * @return 加密后的值
     */
    public static Object fieldEncrypt(EncryptField cryptoString, Object val) {
        if (Objects.isNull(cryptoString)) {
            return val;
        }
        if (val instanceof String) {
            try {
                Class<? extends ICustomEncryptor> rule = cryptoString.rule();
                ICustomEncryptor cryptoRule = rule.newInstance();
                String value = (String) val;
                return cryptoRule.encrypt(value);
            } catch (Exception e) {
                e.printStackTrace();
                return val;
            }
        } else if (val instanceof Collection) {
            Collection<?> coll = (Collection<?>) val;
            if (CollUtil.isNotEmpty(coll)) {
                Class<? extends ICustomEncryptor> rule = cryptoString.rule();
                try {
                    ICustomEncryptor cryptoRule = rule.newInstance();
                    ArrayList<String> resList = CollUtil.newArrayList();
                    for (Object obj : coll) {
                        if (!(obj instanceof String)) {
                            return val;
                        }
                        resList.add(cryptoRule.encrypt((String) obj));
                    }
                    return resList;
                } catch (Exception e) {
                    e.printStackTrace();
                    return val;
                }
            }

        } else if (val.getClass().isArray()) {
            Object[] arr = (Object[]) val;
            if (ArrayUtil.isNotEmpty(arr)) {
                Class<? extends ICustomEncryptor> rule = cryptoString.rule();
                try {
                    ICustomEncryptor cryptoRule = rule.newInstance();
                    for (int i = 0; i < arr.length; i++) {
                        if (!(arr[i] instanceof String)) {
                            return val;
                        }
                        arr[i] = cryptoRule.encrypt((String) arr[i]);
                    }
                    return arr;
                } catch (Exception e) {
                    e.printStackTrace();
                    return val;
                }
            }
        }
        return val;
    }

    /**
     * 单个String入参加密处理
     *
     * @param namespace 方法命名空间
     * @param args      参数
     */
    public static void singleStringEncryptHandle(String namespace, Object[] args, EncryptorCacheManager encryptorCacheManager) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Method method = getMethodByNamespace(namespace);
        EncryptField cryptoString = getCryptoStringByMethod(method, EncryptMode.ENCRYPT);
        if (Objects.isNull(cryptoString)) {
            return;
        }
        if (cryptoString.mode().equals(EncryptMode.DECRYPT)) {
            return;
        }

        ICustomEncryptor cryptoRule = encryptorCacheManager.getEncryptor(cryptoString.rule());
        args[1] = cryptoRule.encrypt((String) args[1]);
    }

    /**
     * 入参为单个 List<String>、String[]入参加密处理
     *
     * @param cryptoString       加密注解
     * @param parameterObjectMap 参数
     */
    public static void stringsEncryptHandle(EncryptKeys cryptoString, Map<String, Object> parameterObjectMap, EncryptorCacheManager encryptorCacheManager) throws InstantiationException, IllegalAccessException {
        EncryptKey[] value = cryptoString.value();
        Object mapValue = null;
        for (Map.Entry<String, Object> mapEntity : parameterObjectMap.entrySet()) {
            Object mapEntityValue = mapEntity.getValue();
            if (!(mapEntityValue instanceof Collection || mapEntityValue.getClass().isArray())) {
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
        if (mapValue instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<Object> coll = (Collection<Object>) mapValue;
            if (CollUtil.isEmpty(coll)) {
                return;
            }
            Object first = CollUtil.getFirst(coll);
            if (Objects.isNull(first)) {
                return;
            }
            if (!(first instanceof String)) {
                return;
            }
            Collection<String> encryptColl = CollUtil.create(String.class);
            ICustomEncryptor cryptoRule = encryptorCacheManager.getEncryptor(value[0].rule());
            for (Object item : coll) {
                encryptColl.add(cryptoRule.encrypt((String) item));
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
            ICustomEncryptor cryptoRule = encryptorCacheManager.getEncryptor(value[0].rule());
            for (int i = 0; i < arr.length; i++) {
                encryptArr[i] = cryptoRule.encrypt((String) arr[i]);
            }
            parameterObjectMap.replaceAll((k, v) -> encryptArr);
        }
    }

    /**
     * 单个String入参加密处理
     *
     * @param namespace 方法命名空间
     * @param args      参数
     */
    public static void singleEntityEncryptHandle(String namespace, Object[] args, EncryptorCacheManager encryptorCacheManager) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object paramObject = args[1];
        Class<?> paramObjectClass = paramObject.getClass();
        EncryptField cryptoClass = AnnotationUtil.getAnnotation(paramObjectClass, EncryptField.class);
        if (Objects.isNull(cryptoClass)) {
            return;
        }

        Object cloneObj = objectClone(paramObject, namespace);
        MybatisEncryptUtils.entityCrypto(cloneObj, EncryptMode.ENCRYPT, encryptorCacheManager);
        args[1] = cloneObj;
    }

    /**
     * 获取方法@CryptoString信息
     */
    public static EncryptField getCryptoStringByMethod(Method method, EncryptMode mode) {
        EncryptFields cryptoStrings = AnnotationUtil.getAnnotation(method, EncryptFields.class);
        if (Objects.isNull(cryptoStrings)) {
            return AnnotationUtil.getAnnotation(method, EncryptField.class);
        } else {
            for (EncryptField cryptoStringItem : cryptoStrings.value()) {
                if (cryptoStringItem.mode().equals(EncryptMode.ALL) || cryptoStringItem.mode().equals(mode)) {
                    return cryptoStringItem;
                }
            }
        }
        return null;
    }

}
