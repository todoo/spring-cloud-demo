package com.easy.ms.router.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easy.cache.Cache;
import com.easy.cache.CacheManager;

@Configuration
public class CacheConfiguration {
    @Autowired
    private CacheManager cacheManager;

    @Bean
    public Cache appGatewayCache() {
        return cacheManager.getCache("gateway");
    }
}
