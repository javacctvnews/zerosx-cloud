package com.zerosx.common.core.config;

import com.zerosx.common.base.constants.TranslConstants;
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

    @Bean(name = TranslConstants.DICT)
    @ConditionalOnMissingBean
    public DictTranslationService dictTranslationService() {
        return new DictTranslationService();
    }

    @Bean(name = TranslConstants.ENUMS)
    @ConditionalOnMissingBean
    public EnumsTranslationService enumsTranslationService() {
        return new EnumsTranslationService();
    }

    @Bean(name = TranslConstants.OPERATOR)
    @ConditionalOnMissingBean
    public OperatorNameTranslationService operatorNameTranslationService() {
        return new OperatorNameTranslationService();
    }

    @Bean(name = TranslConstants.OSS)
    @ConditionalOnMissingBean
    public OssTranslationService ossTranslationService() {
        return new OssTranslationService();
    }

    @Bean(name = TranslConstants.REGION)
    @ConditionalOnMissingBean
    public RegionTranslationService regionTranslationService() {
        return new RegionTranslationService();
    }

    @Bean(name = TranslConstants.DEPT)
    @ConditionalOnMissingBean
    public DeptTranslationService deptTranslationService() {
        return new DeptTranslationService();
    }

}
