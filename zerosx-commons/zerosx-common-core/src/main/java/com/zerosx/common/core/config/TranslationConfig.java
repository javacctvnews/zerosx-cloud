package com.zerosx.common.core.config;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.core.translation.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * TranslationConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-07 00:20
 **/
@Slf4j
@AutoConfiguration
public class TranslationConfig {

    @Bean(name = CommonConstants.TRANS_DICT)
    @ConditionalOnMissingBean
    public DictTranslationService dictTranslationService() {
        return new DictTranslationService();
    }

    @Bean(name = CommonConstants.TRANS_ENUMS)
    @ConditionalOnMissingBean
    public EnumsTranslationService enumsTranslationService() {
        return new EnumsTranslationService();
    }

    @Bean(name = CommonConstants.TRANS_OPERATOR_ID)
    @ConditionalOnMissingBean
    public OperatorNameTranslationService operatorNameTranslationService() {
        return new OperatorNameTranslationService();
    }

    @Bean(name = CommonConstants.TRANS_OSS)
    @ConditionalOnMissingBean
    public OssTranslationService ossTranslationService() {
        return new OssTranslationService();
    }

    @Bean(name = CommonConstants.TRANS_REGION)
    @ConditionalOnMissingBean
    public RegionTranslationService regionTranslationService() {
        return new RegionTranslationService();
    }

}
