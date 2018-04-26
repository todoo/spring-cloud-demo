package com.easy.ms.distributedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.easy.*" })
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

