package com.zerosx.common.core.translation.impl;

import com.zerosx.api.resource.IOssFileUploadClient;
import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.oss.model.OssObjectVO;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.common.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * OssTranslationService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-04 16:10
 **/
@Slf4j
public class OssTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return TranslConstants.OSS;
    }

    @Override
    public String translation(Object key, String other) {
        return translation(key);
    }

    @Override
    protected String getRedissonCache(String objectName) {
        String ossFileKey = ZCache.OSS_FILE_URL.key(objectName);
        OssObjectVO ossObjectVO;
        try {
            String ossObjectStr = getRedissonOpService().get(ossFileKey);
            ossObjectVO = JacksonUtil.toObject(ossObjectStr, OssObjectVO.class);
        } catch (Exception e) {
            getRedissonOpService().del(ossFileKey);
            return EMPTY;
        }
        if (ossObjectVO != null && !ossObjectVO.expired()) {
            return ossObjectVO.getObjectViewUrl();
        }
        return EMPTY;
    }

    @Override
    protected ResultVO<String> getFeignService(String key) {
        return SpringUtils.getBean(IOssFileUploadClient.class).getObjectViewUrl(key);
    }

}
