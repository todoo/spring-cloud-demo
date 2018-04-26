package com.easy.ms.distributedlock.impl;

import com.easy.ms.distributedlock.DistributedLock;

public abstract class AbstractDistributedLockImpl implements DistributedLock {
    public static final int DEFAULT_RETRYTIMES = Integer.MAX_VALUE;
    public static final long DEFAULT_SLEEPMILLIS = 500;
    public static final long DEFAULT_EXPIRE = 30000;

    @Override
    public boolean lock(String key) {
        return this.lock(key, DEFAULT_RETRYTIMES);
    }

    @Override
    public boolean lock(String key, int retryTimes) {
        return this.lock(key, retryTimes, DEFAULT_SLEEPMILLIS);
    }

    @Override
    public boolean lock(String key, int retryTimes, long sleepMillis) {
        return this.lock(key, DEFAULT_EXPIRE, retryTimes, sleepMillis);
    }

    @Override
    public boolean lock(String key, long expire) {
        return this.lock(key, expire, DEFAULT_RETRYTIMES);
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes) {
        return this.lock(key, expire, retryTimes, DEFAULT_SLEEPMILLIS);
    }
}
