package com.zerosx.common.core.translation.impl;

import com.zerosx.api.system.ISysDeptClient;
import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.utils.SpringUtils;

/**
 * DeptTranslationService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-04 16:10
 **/
public class DeptTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return TranslConstants.DEPT;
    }

    @Override
    public String translation(Object key, String other) {
        return translation(key);
    }

    @Override
    protected String getRedissonCache(String key) {
        return getRedissonOpService().hGet(ZCache.DEPT.key(key), TranslConstants.DEPT_NAME);
    }

    @Override
    protected ResultVO<?> getFeignService(String key) {
        return SpringUtils.getBean(ISysDeptClient.class).queryName(Long.parseLong(key));
    }

}
