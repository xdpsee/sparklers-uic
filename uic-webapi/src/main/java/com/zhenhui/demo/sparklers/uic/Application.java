package com.zhenhui.demo.sparklers.uic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableEurekaClient
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.zhenhui.demo.sparklers")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}




