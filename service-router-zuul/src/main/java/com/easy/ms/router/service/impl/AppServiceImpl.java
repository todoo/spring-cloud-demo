package com.easy.ms.router.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.cache.Cache;
import com.easy.ms.router.constants.CacheKeyPrefix;
import com.easy.ms.router.model.mongo.ApplicationInfo;
import com.easy.ms.router.repository.ApplicationInfoRepository;
import com.easy.ms.router.service.AppService;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private ApplicationInfoRepository applicationInfoRepository;
    @Autowired
    private Cache appGatewayCache;

    @Override
    public String getAppRsaPublicKey(String appId) {

        ApplicationInfo applicationInfo = this.getApplicationInfoByAppId(appId);
        if (applicationInfo == null) {
            return null;
        }

        return applicationInfo.getRsaPublicKey();
    }

    @Override
    public String getServerRsaPrivateKey(String appId) {
        ApplicationInfo applicationInfo = this.getApplicationInfoByAppId(appId);
        if (applicationInfo == null) {
            return null;
        }
        return applicationInfo.getServerPrivateKey();
    }

    private ApplicationInfo getApplicationInfoByAppId(String appId) {
        // 缓存获取
        String key = CacheKeyPrefix.APPINFO + appId;
        ApplicationInfo applicationInfo = (ApplicationInfo) appGatewayCache.get(key);
        if (applicationInfo != null && StringUtils.isNotBlank(applicationInfo.getRsaPublicKey())) {
            return applicationInfo;
        }

        // 缓存不存在，mongo中获取
        applicationInfo = applicationInfoRepository.findByAppId(appId);
        if (applicationInfo == null) {
            return null;
        }

        // 存入缓存
        appGatewayCache.put(key, applicationInfo, 5 * 60000l);

        return applicationInfo;
    }

}
