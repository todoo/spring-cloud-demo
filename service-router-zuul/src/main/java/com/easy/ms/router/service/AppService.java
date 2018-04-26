package com.easy.ms.router.service;

public interface AppService {
    public String getAppRsaPublicKey(String appId);
    
    public String getServerRsaPrivateKey(String appId);
}
