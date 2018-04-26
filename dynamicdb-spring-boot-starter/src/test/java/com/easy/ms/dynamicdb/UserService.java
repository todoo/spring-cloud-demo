package com.easy.ms.dynamicdb;

public interface UserService {
    public User findById(Long id);
    
    public User findByIdDefault(Long id);
    
    public User test(Long id);
    
    public void testTransactional(Long id);
}
