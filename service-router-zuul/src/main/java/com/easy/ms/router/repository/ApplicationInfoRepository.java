package com.easy.ms.router.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.easy.ms.router.model.mongo.ApplicationInfo;

public interface ApplicationInfoRepository extends MongoRepository<ApplicationInfo, String>{
    @Query("{'appId':?0}")
    public ApplicationInfo findByAppId(String appId);
}
