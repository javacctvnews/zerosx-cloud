package com.zerosx.encrypt2.config;

import com.zerosx.encrypt2.core.interceptor.EncryptInterceptor;
import com.zerosx.encrypt2.core.properties.EncryptProperties;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * mybatis拦截器配置
 */
@Configuration
public class MybatisAutoConfiguration {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;
    @Autowired
    private EncryptProperties encryptProperties;

    @PostConstruct
    public void addMysqlInterceptor() {
        //创建⾃定义mybatis拦截器，添加到chain的最后⾯
        EncryptInterceptor mybatisEncryptInterceptor = new EncryptInterceptor(encryptProperties);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(mybatisEncryptInterceptor);
        }
    }
}
