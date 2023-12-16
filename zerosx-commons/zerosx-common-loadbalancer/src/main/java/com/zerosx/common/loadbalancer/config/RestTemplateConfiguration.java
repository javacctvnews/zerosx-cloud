package com.zerosx.common.loadbalancer.config;

import com.zerosx.common.loadbalancer.interceptor.FeignHttpInterceptor;
import com.zerosx.common.loadbalancer.properties.RestTemplateProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 采用HttpClient
 * 查看是否生效：FeignClientFactoryBean.java:375
 */
@AutoConfiguration
@EnableConfigurationProperties({RestTemplateProperties.class, FeignHttpClientProperties.class})
public class RestTemplateConfiguration {

    @Autowired
    private RestTemplateProperties restTemplateProperties;
    @Autowired
    private FeignHttpClientProperties feignHttpClientProperties;

    @Bean
    @ConditionalOnMissingBean
    public FeignHttpInterceptor feignHttpInterceptor() {
        return new FeignHttpInterceptor();
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

    /**
     * httpclient 实现的ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory httpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        //连接超时
        clientHttpRequestFactory.setConnectTimeout(feignHttpClientProperties.getConnectionTimeout());
        //数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(restTemplateProperties.getReadTimeout());
        //连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(restTemplateProperties.getConnectionRequestTimeout());
        //缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
        clientHttpRequestFactory.setBufferRequestBody(true);
        return clientHttpRequestFactory;
    }

    /**
     * 使用连接池的 httpclient
     */
    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        // 最大链接数
        connectionManager.setMaxTotal(feignHttpClientProperties.getMaxConnections());
        // 同路由并发数
        connectionManager.setDefaultMaxPerRoute(feignHttpClientProperties.getMaxConnectionsPerRoute());
        RequestConfig requestConfig = RequestConfig.custom()
                // 读超时
                .setSocketTimeout(restTemplateProperties.getReadTimeout())
                // 链接超时
                .setConnectTimeout(feignHttpClientProperties.getConnectionTimeout())
                // 链接不够用的等待时间
                .setConnectionRequestTimeout(restTemplateProperties.getReadTimeout())
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .evictIdleConnections(30, TimeUnit.SECONDS)
                //重试次数，默认是3次，没有开启  不开启
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, false))
                //使用请求Keep-Alive的值，没有的话永久有效
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                .disableAutomaticRetries().build();
    }

}
