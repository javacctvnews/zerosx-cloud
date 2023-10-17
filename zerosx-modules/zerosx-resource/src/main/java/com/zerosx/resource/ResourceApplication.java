package com.zerosx.resource;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@MapperScan(basePackages = "com.zerosx.resource.mapper")
@ComponentScan(basePackages = {"com.zerosx"})
@EnableFeignClients(basePackages = {"com.zerosx"})
@SpringBootApplication
public class ResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }

}
