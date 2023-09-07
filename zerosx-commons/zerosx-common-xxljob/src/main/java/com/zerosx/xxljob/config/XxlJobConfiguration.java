package com.zerosx.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.zerosx.xxljob.properties.XxlJobConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * XxlJobConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-07 13:30
 **/
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties({XxlJobConfigProperties.class})
public class XxlJobConfiguration {

    @Autowired
    private XxlJobConfigProperties xxlJobConfigProperties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobConfigProperties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobConfigProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(xxlJobConfigProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(xxlJobConfigProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobConfigProperties.getExecutor().getPort() + 1000);
        xxlJobSpringExecutor.setAccessToken(xxlJobConfigProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobConfigProperties.getExecutor().getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobConfigProperties.getExecutor().getLogretentiondays());
        log.info("初始化【XxlJobSpringExecutor】成功，调度中心地址：{}", xxlJobConfigProperties.getAdmin().getAddresses());
        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */


}
