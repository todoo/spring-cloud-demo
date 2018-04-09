package com.easy.cache;

import java.io.IOException;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.easy.cache.impl.EhRedisCacheManager;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest{
    @Autowired
    EhRedisCacheManager ehRedisCacheManager;
    
    //@Test
    public void testPut() {
        Cache cache = ehRedisCacheManager.getCache("test");
        cache.put("test", "aaaa");
        
        try {
            Thread.sleep(40000l);
            String value = (String) cache.get("test");
            System.out.println(value);
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    //@Test
    public void testPutWithTime() {
        Cache cache = ehRedisCacheManager.getCache("test");
        cache.put("test", "aaaa", 20000l);
        
        try {
            Thread.sleep(40000l);
            String value = (String) cache.get("test");
            System.out.println(value);
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    //@Test
    public void testGet() {
        Cache cache = ehRedisCacheManager.getCache("test");
        cache.put("test", "aaaa");
        String value = (String) cache.get("test");
        System.out.println(value);
    }
    
    //@Test
    public void testGetWithTime() throws InterruptedException {
        Cache cache = ehRedisCacheManager.getCache("test");
        cache.put("test", "aaaa",20000l);
        String value = (String) cache.get("test");
        System.out.println(value);
        Thread.sleep(2000l);
        value = (String) cache.get("test");
        System.out.println(value);
    }
    
    //@Test
    public void testEvict() {
        Cache cache = ehRedisCacheManager.getCache("test");
        cache.evict("test");
    }
}
