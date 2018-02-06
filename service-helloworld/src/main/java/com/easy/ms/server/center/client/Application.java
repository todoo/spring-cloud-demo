package com.easy.ms.server.center.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = { "com.easy.*" })
@EnableEurekaClient
@RestController
public class Application {
    @Value("${server.port}")
    private String port;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @RequestMapping("/helloworld")
    public String home() {
        return "Hello world from " + port;
    }
}
