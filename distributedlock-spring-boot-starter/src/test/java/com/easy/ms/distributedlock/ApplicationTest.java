package com.easy.ms.distributedlock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest{
    @Autowired
    DistributedLock redisDistributedLock;
    
    @Test
    public void testPut() {
        boolean locked = redisDistributedLock.lock("testkey");
        System.out.println(locked);
        try {
            Thread.sleep(30000l);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean released = redisDistributedLock.releaseLock("testkey");
        System.out.println(released);
    }
    
}
