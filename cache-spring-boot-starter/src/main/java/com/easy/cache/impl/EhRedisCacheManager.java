package com.easy.cache.impl;

import java.util.HashMap;
import java.util.Map;

import com.easy.cache.Cache;
import com.easy.cache.CacheManager;

public class EhRedisCacheManager implements CacheManager {
    private Map<String, EhRedisCacheImpl> ehRedisCacheMap = new HashMap<>();

    public EhRedisCacheManager(Map<String, EhRedisCacheImpl> ehRedisCacheMap) {
        this.ehRedisCacheMap = ehRedisCacheMap;
    }

    @Override
    public Cache getCache(String name) {
        return this.ehRedisCacheMap.get(name);
    }
}
