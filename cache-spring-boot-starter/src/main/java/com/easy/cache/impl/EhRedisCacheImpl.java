package com.easy.cache.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.easy.cache.Cache;
import com.easy.cache.constant.RedisMessageChannelConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EhRedisCacheImpl implements Cache {
    private String name;
    private EhCacheCacheManager ehcacheManager;
    private RedisTemplate<String, Object> redisTemplate;
    private ConcurrentHashMap<String, Long> cacheDeadline = new ConcurrentHashMap<>();

    public EhRedisCacheImpl(String name, EhCacheCacheManager ehcacheManager,
            RedisTemplate<String, Object> redisTemplate) {
        this.name = name;
        this.ehcacheManager = ehcacheManager;
        this.redisTemplate = redisTemplate;
    }

    private String getKey(String key) {
        return "EHREDIS:" + this.name + ":" + key;
    }

    private String getCacheUpdateChannel() {
        return RedisMessageChannelConstant.CACHE_UPDATE_CHANNEL;
    }

    private void sendCacheUpdateRedisMessage(String key) {
        // 发送消息，刷新分布式环境中的所有ehcache缓存
        EhRedisCacheUpdateMessage message = new EhRedisCacheUpdateMessage();
        message.setCacheName(name);
        message.setUpdateCacheKey(key);
        redisTemplate.convertAndSend(this.getCacheUpdateChannel(), message);
    }

    @Override
    public void put(String key, Object value) {
        String cacheKey = this.getKey(key);
        log.info("set cache to redis: {}", cacheKey);
        redisTemplate.boundValueOps(cacheKey).set(value);

        // 发送缓存更新消息,用于分布式环境ehcache缓存同步更新
        this.sendCacheUpdateRedisMessage(key);
    }

    @Override
    public Object get(String key) {
        String cacheKey = this.getKey(key);
        org.springframework.cache.Cache ehcache = ehcacheManager.getCache(name);
        if (ehcache == null) {
            log.error("ehcache {} not found", name);
        } else {
            ValueWrapper valueWrapper = ehcache.get(cacheKey);
            if (valueWrapper != null) {
                // 是否超时
                if (this.cacheDeadline.containsKey(cacheKey) && System.currentTimeMillis() < this.cacheDeadline.get(cacheKey)) {
                    log.info("get cache from ehcache: {}", cacheKey);
                    return valueWrapper.get();
                } else if (!this.cacheDeadline.containsKey(cacheKey)) {
                    //不存在超时时间
                    return valueWrapper.get();
                }
            }
        }
        log.info("get cache from redis: {}", cacheKey);
        BoundValueOperations<String, Object> op = this.redisTemplate.boundValueOps(cacheKey);
        Object value = op.get();
        if (value != null && ehcache != null) {
            // 写入ehcache
            ehcache.put(cacheKey, value);
            // 将到期时间保存在内存，用于ehcache判断
            if (op.getExpire() > 0) {
                Long deadline = System.currentTimeMillis() + op.getExpire() * 1000;
                this.cacheDeadline.put(cacheKey, deadline);
            }         
        }

        return value;
    }

    @Override
    public void evict(String key) {
        String cacheKey = this.getKey(key);
        org.springframework.cache.Cache ehcache = ehcacheManager.getCache(name);
        if (ehcache == null) {
            log.error("ehcache {} not found", name);
        } else {
            log.info("evict cache from ehcache: {}", cacheKey);
            ehcache.evict(cacheKey);
            // 去掉截止时间
            if (this.cacheDeadline.containsKey(cacheKey)) {
                this.cacheDeadline.remove(cacheKey);
            }
        }

        log.info("evict cache from redis: {}", cacheKey);
        this.redisTemplate.delete(cacheKey);

        // 发送缓存更新消息,用于分布式环境ehcache缓存同步更新
        this.sendCacheUpdateRedisMessage(key);
    }

    @Override
    public void put(String key, Object value, Long milliseconds) {
        String cacheKey = this.getKey(key);
        log.info("set cache to redis: {}", cacheKey);
        redisTemplate.boundValueOps(cacheKey).set(value, milliseconds, TimeUnit.MILLISECONDS);

        // 发送缓存更新消息,用于分布式环境ehcache缓存同步更新
        this.sendCacheUpdateRedisMessage(key);
    }

    @Override
    public void updateLevel2Cache(String key) {
        String cacheKey = this.getKey(key);
        org.springframework.cache.Cache ehcache = ehcacheManager.getCache(name);
        if (ehcache == null) {
            return;
        }
        // 删除存在的ehcache缓存
        ehcache.evict(cacheKey);
        // 去掉截止时间
        if (this.cacheDeadline.containsKey(cacheKey)) {
            this.cacheDeadline.remove(cacheKey);
        }

        // 重新缓存
        this.get(key);
    }

}
