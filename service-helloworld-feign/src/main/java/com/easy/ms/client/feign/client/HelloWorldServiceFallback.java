package com.easy.ms.client.feign.client;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldServiceFallback implements HelloWorldService {

    @Override
    public String helloworld() {
        return "hello world fallback";
    }

}
