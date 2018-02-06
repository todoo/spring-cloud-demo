package com.easy.ms.client.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${micro-services.helloworld.id}", fallback = HelloWorldServiceFallback.class)
public interface HelloWorldService {
    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    String helloworld();
}
