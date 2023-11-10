package com.zerosx.sas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.zerosx")
@EnableFeignClients(basePackages = {"com.zerosx"})
@SpringBootApplication
public class SasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SasApplication.class, args);
    }

}
