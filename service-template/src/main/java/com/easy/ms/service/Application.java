package com.easy.ms.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = { "com.easy.*" })
@EnableEurekaClient
public class Application {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(Application.class, args);
    }
}
