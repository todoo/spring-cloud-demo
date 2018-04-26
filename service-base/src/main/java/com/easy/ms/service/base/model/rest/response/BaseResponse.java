package com.easy.ms.service.base.model.rest.response;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2996137653148090308L;

    /**
     * 结果码
     */
    @NotBlank
    private String code;
    /**
     * 结果码描述
     */
    private String msg;
}
