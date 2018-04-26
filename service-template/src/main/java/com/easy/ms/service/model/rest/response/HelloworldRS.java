package com.easy.ms.service.model.rest.response;

import com.easy.ms.service.base.model.rest.response.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class HelloworldRS extends BaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -427772709786238214L;
    private String reply;
}
