package com.easy.ms.dynamicdb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select o from User o where o.id = ?1")
    public User findById(Long id);
}
