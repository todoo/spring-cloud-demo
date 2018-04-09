package com.easy.cache;

public interface Cache {
    public void put(String key, Object value);

    public Object get(String key);
    
    public void evict(String key);
    
    public void put(String key, Object value, Long milliseconds);
    
    public void updateLevel2Cache(String key);
}
