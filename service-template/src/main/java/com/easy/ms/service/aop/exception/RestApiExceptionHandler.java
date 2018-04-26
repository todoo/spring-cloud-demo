package com.easy.ms.service.aop.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.easy.ms.service.base.model.rest.response.BaseResponse;
import com.easy.ms.service.constant.ResultCodeEnum;
import com.easy.ms.service.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler {
    
    /**
     * 参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse restApiRequestError(MethodArgumentNotValidException e) {
        log.error("restApiRequestError:", e);
        BaseResponse rs = new BaseResponse();
        rs.setCode(ResultCodeEnum.REQUEST_PARAM_ERROR.getCode());
        rs.setMsg(ResultCodeEnum.REQUEST_PARAM_ERROR.getDescription());
        return rs;
    }
    
    /**
     * 业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse restApiBusinessError(BusinessException e) {
        log.error("restApiBusinessError:", e);
        BaseResponse rs = new BaseResponse();
        rs.setCode(e.getCode());
        rs.setMsg(ResultCodeEnum.fromCode(e.getCode()).getDescription());
        return rs;
    }
}
