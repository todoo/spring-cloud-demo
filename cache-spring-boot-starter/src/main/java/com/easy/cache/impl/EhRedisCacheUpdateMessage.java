package com.easy.cache.impl;

import java.io.Serializable;

public class EhRedisCacheUpdateMessage implements Serializable {
    private static final long serialVersionUID = 6475477751348074304L;
    private String cacheName;
    private String updateCacheKey;
    
    public String getCacheName() {
        return cacheName;
    }
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
    public String getUpdateCacheKey() {
        return updateCacheKey;
    }
    public void setUpdateCacheKey(String updateCacheKey) {
        this.updateCacheKey = updateCacheKey;
    }
    
}
