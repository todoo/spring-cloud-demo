package com.easy.ms.distributedlock.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.easy.ms.distributedlock.impl.RedisDistributedLockImpl;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class DistributedLockAutoConfiguration {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Bean
    @ConditionalOnMissingBean(RedisDistributedLockImpl.class)
    public RedisDistributedLockImpl redisDistributedLock() {
        RedisDistributedLockImpl redisDistributedLockImpl = new RedisDistributedLockImpl(redisTemplate);
        return redisDistributedLockImpl;
    }
}
