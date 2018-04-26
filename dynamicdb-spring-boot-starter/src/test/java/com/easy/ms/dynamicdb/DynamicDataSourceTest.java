package com.easy.ms.dynamicdb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@EnableAutoConfiguration
public class DynamicDataSourceTest {
    @Autowired
    private UserService userService;
    
    //@Test
    public void testDs() {
        User user = userService.findById(1l);
        System.out.println(user);
        user = userService.findByIdDefault(1l);
        System.out.println(user);
        DataSourceTransactionManager a = null;
    }
    
    @Test
    public void testTransactional() {
        userService.testTransactional(1l);
    }
}
