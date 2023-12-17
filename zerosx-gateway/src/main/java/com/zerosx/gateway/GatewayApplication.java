package com.zerosx.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import reactor.netty.ReactorNetty;

@ComponentScan(basePackages = "com.zerosx")
@EnableFeignClients(basePackages = {"com.zerosx"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {

    public static void main(String[] args) {
        // 这里工作线程数为1-4倍都可以。看具体情况
        //System.setProperty(ReactorNetty.IO_WORKER_COUNT, String.valueOf(Math.max(Runtime.getRuntime().availableProcessors(), 4)));

        //不是-1即可
        System.getProperty(ReactorNetty.IO_SELECT_COUNT, "1");

        SpringApplication.run(GatewayApplication.class, args);
    }

}
