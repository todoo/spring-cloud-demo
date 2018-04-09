package com.easy.cache.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import com.easy.cache.constant.RedisMessageChannelConstant;
import com.easy.cache.impl.EhRedisCacheImpl;
import com.easy.cache.impl.EhRedisCacheManager;
import com.easy.cache.impl.EhRedisCacheUpdateSub;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class EhRedisCacheAutoConfiguration {
    @Autowired
    private EhCacheCacheManager ehcacheManager;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    public EhRedisCacheManager ehRedisCacheManager() {
        Map<String, EhRedisCacheImpl> ehRedisCacheMap = new HashMap<>();
        Collection<String> ehcacheNames = ehcacheManager.getCacheNames();
        Iterator<String> ehcacheNamesIterator = ehcacheNames.iterator();
        while (ehcacheNamesIterator.hasNext()) {
            String ehcacheName = ehcacheNamesIterator.next();
            EhRedisCacheImpl ehRedisCacheImpl = new EhRedisCacheImpl(ehcacheName, ehcacheManager, redisTemplate);
            ehRedisCacheMap.put(ehcacheName, ehRedisCacheImpl);
        }
        EhRedisCacheManager ehRedisCacheManager = new EhRedisCacheManager(ehRedisCacheMap);

        return ehRedisCacheManager;
    }
    
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, EhRedisCacheManager ehRedisCacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        MessageListener messageListener = new EhRedisCacheUpdateSub(ehRedisCacheManager);
        Topic topic = new PatternTopic(RedisMessageChannelConstant.CACHE_UPDATE_CHANNEL);
        redisMessageListenerContainer.addMessageListener(messageListener, topic);
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        return redisMessageListenerContainer;
    }
}
