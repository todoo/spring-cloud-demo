package com.easy.ms.distributedlock.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * redis实现分布式锁
 * @author tkx
 *
 */
@Slf4j
public class RedisDistributedLockImpl extends AbstractDistributedLockImpl {
    private RedisTemplate<String, Object> redisTemplate;
    private ThreadLocal<String> value = new ThreadLocal<String>();
    public static final String REDIS_LOCK_KEY_PREFIX = "REDIS:DISTRIBUTED:LOCK:";

    public RedisDistributedLockImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean locked = this.setRedis(key, expire);
        log.info("key lock: {}-{}", key, locked);
        while (!locked && retryTimes-- > 0) {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            locked = this.setRedis(key, expire);
            log.info("key lock: {}-{}", key, locked);
        }
        return locked;
    }

    private boolean setRedis(String key, long expire) {
        key = REDIS_LOCK_KEY_PREFIX + key;
        UUID uuid = UUID.randomUUID();
        value.set(uuid.toString());
        boolean success = redisTemplate.opsForValue().setIfAbsent(key, value.get());
        if (success) {
            redisTemplate.boundValueOps(key).expire(expire, TimeUnit.MILLISECONDS);
        }

        return success;
    }

    @Override
    public boolean releaseLock(String key) {
        try {
            key = REDIS_LOCK_KEY_PREFIX + key;
            String redisValue = (String) redisTemplate.boundValueOps(key).get();
            if (value.get() == null || !value.get().equals(redisValue)) {
                // 不是当前需要释放的锁，原锁已过期，不用主动释放
                log.info("lock value not exist");
                return false;
            }
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("release lock error:", e);
            return false;
        }
        return true;
    }

}
