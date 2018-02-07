package com.easy.ms.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.easy.ms.router.filter.post.LogFilter;
import com.easy.ms.router.filter.pre.AuthFilter;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }
    
    @Bean
    public LogFilter logFilter() {
        return new LogFilter();
    }
}
