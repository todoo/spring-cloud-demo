package com.easy.ms.dynamicdb;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Resource(name = "sysJdbcTemplate")
    private JdbcTemplate sysJdbcTemplate;
    @Resource(name = "busJdbcTemplate")
    private JdbcTemplate busJdbcTemplate;
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @TargetDataSource("ds1")
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByIdDefault(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User test(Long id) {
        UserService userService = AopContext.currentProxy() != null ? (UserService) AopContext.currentProxy() : this;
        return userService.findById(id);
    }

    @Transactional
    public void testTransactional(Long id) {
        UserServiceImpl userServiceImpl = AopContext.currentProxy() != null
                ? (UserServiceImpl) AopContext.currentProxy() : this;
                
        userServiceImpl.deleteByIdDefault(1l);
        userServiceImpl.deleteById(2l);
        System.out.print("a");
    }

    @TargetDataSource("ds1")
    public void deleteById(Long id) {
        //userRepository.delete(id);
        // throw new RuntimeException();
        //DynamicDataSourceContextHolder.setCurrentDataSourceId("ds1");
        this.jdbcTemplate.update("delete from user where id = 2");
    }

    public void deleteByIdDefault(Long id) {
        //userRepository.delete(id);
        // throw new RuntimeException();
        this.jdbcTemplate.update("delete from user where id = 1");
    }
}
