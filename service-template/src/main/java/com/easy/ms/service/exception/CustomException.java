package com.easy.ms.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 * 
 * @author tkx
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CustomException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3164301437141961692L;
    private String code;
    
    public CustomException(String code) {
        this.code = code;
    }
}
