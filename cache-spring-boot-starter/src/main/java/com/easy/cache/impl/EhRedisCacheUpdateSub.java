package com.easy.cache.impl;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.easy.cache.Cache;
import com.easy.cache.util.RedisObjectSerializer;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class EhRedisCacheUpdateSub implements MessageListener {
    private EhRedisCacheManager ehRedisCacheManager;
    
    public EhRedisCacheUpdateSub(EhRedisCacheManager ehRedisCacheManager) {
        this.ehRedisCacheManager = ehRedisCacheManager;
    }
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisObjectSerializer redisObjectSerializer = new RedisObjectSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        String channel = stringRedisSerializer.deserialize(message.getChannel());
        log.info("redis message channel: {}", channel);
            
        EhRedisCacheUpdateMessage updateMessage = (EhRedisCacheUpdateMessage) redisObjectSerializer.deserialize(message.getBody());
        Cache ehRedisCache = ehRedisCacheManager.getCache(updateMessage.getCacheName());
        ehRedisCache.updateLevel2Cache(updateMessage.getUpdateCacheKey());
    }

}
