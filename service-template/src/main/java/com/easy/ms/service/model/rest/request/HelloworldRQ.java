package com.easy.ms.service.model.rest.request;

import javax.validation.constraints.NotBlank;

import com.easy.ms.service.base.model.rest.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class HelloworldRQ extends BaseRequest{
    /**
     * 
     */
    private static final long serialVersionUID = -3671428469057203555L;
    @NotBlank(message="用户名不能为空")
    private String username;    
}
