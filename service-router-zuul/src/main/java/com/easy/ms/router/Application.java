package com.easy.ms.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.easy.ms.router.filter.post.LogFilter;
import com.easy.ms.router.filter.pre.AuthFilter;
import com.easy.ms.router.filter.pre.RestApiSignValidateFilter;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    //@Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }
    
    //@Bean
    public LogFilter logFilter() {
        return new LogFilter();
    }
    
    //@Bean
    public RestApiSignValidateFilter signValidateFilter() {
        return new RestApiSignValidateFilter();
    }
}
