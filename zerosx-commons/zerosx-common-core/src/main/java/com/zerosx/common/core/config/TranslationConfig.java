package com.zerosx.common.core.config;

import com.zerosx.common.core.translation.ITranslationService;
import com.zerosx.common.core.translation.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.Arrays;

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


    @Bean
    @ConditionalOnMissingBean
    public DictTranslationService dictTranslationService() {
        return new DictTranslationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public EnumsTranslationService enumsTranslationService() {
        return new EnumsTranslationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public OperatorNameTranslationService operatorNameTranslationService() {
        return new OperatorNameTranslationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public OssTranslationService ossTranslationService() {
        return new OssTranslationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public RegionTranslationService regionTranslationService() {
        return new RegionTranslationService();
    }


    @Autowired(required = false)
    public ITranslationService[] translationServices;

    @PostConstruct
    public void init() {
        Arrays.stream(translationServices).forEach((e) -> {
            AbsTranslationService.transBeanCache.put(e.translationType(), e);
            log.debug("加载翻译接口实现Bean:{}", e.translationType());
        });
    }

}
