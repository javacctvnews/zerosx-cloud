package com.zerosx.common.db.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.db.properties.MybatisPlusAutoFillProperties;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自定义填充公共字段
 */
public class CustomMetaObjectHandler implements MetaObjectHandler {

    private MybatisPlusAutoFillProperties autoFillProperties;

    public CustomMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    /**
     * 是否开启了插入填充
     */
    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    /**
     * 是否开启了更新填充
     */
    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    /**
     * 插入填充，字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(), metaObject);
        Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(), metaObject);
        Date date = new Date();
        if (createTime == null) {
            setFieldValByName(autoFillProperties.getCreateTimeField(), date, metaObject);
        }
        if (updateTime == null) {
            setFieldValByName(autoFillProperties.getUpdateTimeField(), date, metaObject);
        }

        Object createBy = getFieldValByName(autoFillProperties.getCreateByField(), metaObject);
        if (createBy == null) {
            setFieldValByName(autoFillProperties.getCreateByField(), ZerosSecurityContextHolder.getUserName(), metaObject);
        }

        Object updateBy = getFieldValByName(autoFillProperties.getUpdateByField(), metaObject);
        if (updateBy == null) {
            setFieldValByName(autoFillProperties.getUpdateByField(), ZerosSecurityContextHolder.getUserName(), metaObject);
        }
    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
        setFieldValByName(autoFillProperties.getUpdateByField(), ZerosSecurityContextHolder.getUserName(), metaObject);
    }



}