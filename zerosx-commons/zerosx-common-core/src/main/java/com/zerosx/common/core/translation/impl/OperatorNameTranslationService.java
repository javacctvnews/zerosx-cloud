package com.zerosx.common.core.translation.impl;

import com.zerosx.api.system.IMutiTenancyGroupClient;
import com.zerosx.api.system.vo.MutiTenancyGroupBO;
import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName OperatorNameTranslationService
 * @Description 租户ID翻译成名称
 * @Author javacctvnews
 * @Date 2023/5/26 13:05
 * @Version 1.0
 */
@Slf4j
public class OperatorNameTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return TranslConstants.OPERATOR;
    }

    @Override
    public String translation(Object key, String other) {
        return translation(key);
    }

    @Override
    protected String getRedissonCache(String key) {
        return getRedissonOpService().hGet(ZCache.OPERATOR.key(key), TranslConstants.OPERATOR_NAME);
    }

    @Override
    protected ResultVO<?> getFeignService(String key) {
        ResultVO<MutiTenancyGroupBO> mutiTenancyGroupBOResultVO = SpringUtils.getBean(IMutiTenancyGroupClient.class).queryOperator(key);
        if (mutiTenancyGroupBOResultVO.getData() == null) {
            return ResultVOUtil.success("");
        }
        return ResultVOUtil.success(mutiTenancyGroupBOResultVO.getData().getTenantGroupName());
    }
}
