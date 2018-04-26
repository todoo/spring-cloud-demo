package com.easy.ms.distributedlock;

public interface DistributedLock {
    public boolean lock(String key);

    public boolean lock(String key, int retryTimes);

    public boolean lock(String key, int retryTimes, long sleepMillis);

    public boolean lock(String key, long expire);

    public boolean lock(String key, long expire, int retryTimes);

    /**
     * 
     * @param lockResource 锁资源
     * @param expire 锁超时时间
     * @param retryTimes 重试次数
     * @param sleepMillis 获取锁失败后的休眠时间
     * @return
     */
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    public boolean releaseLock(String key);
}
