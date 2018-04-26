package com.easy.ms.service.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easy.ms.service.constant.ResultCodeEnum;
import com.easy.ms.service.model.rest.request.HelloworldRQ;
import com.easy.ms.service.model.rest.response.HelloworldRS;

@RestController
public class HelloworldController {
    @RequestMapping("/hello")
    public HelloworldRS hello(@Valid @RequestBody HelloworldRQ request) {
        HelloworldRS rs = new HelloworldRS();
        rs.setCode(ResultCodeEnum.SUCCESS.getCode());
        rs.setMsg(ResultCodeEnum.SUCCESS.getDescription());
        rs.setReply("hello," + request.getUsername());
        return rs;
    }
}
