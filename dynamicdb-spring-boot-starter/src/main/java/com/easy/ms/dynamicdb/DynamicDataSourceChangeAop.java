package com.easy.ms.dynamicdb;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-1)
public class DynamicDataSourceChangeAop {
    
    @Before("@annotation(targetDataSource)")
    public void dynamicDataSourceChange(JoinPoint jp, TargetDataSource targetDataSource){
        if (DynamicDataSourceContextHolder.containsDataSource(targetDataSource.value())) {
            DynamicDataSourceContextHolder.setCurrentDataSourceId(targetDataSource.value());
        }
        
    }
    
    @After("@annotation(targetDataSource)")
    public void dynamicDataSourceClear(JoinPoint jp, TargetDataSource targetDataSource){
        DynamicDataSourceContextHolder.clearDataSourceType(); 
    }
}
