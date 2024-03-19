package com.foitcc.framework.response;


import com.foitcc.common.model.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.NoResourceFoundException;
//todo remove printStackTrace()
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public CommonResult<Void> exception(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        exception.printStackTrace();
        return CommonResult.failed(exception.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public CommonResult<Void> exception(HttpServletRequest request,HttpServletResponse response,Exception exception) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        exception.printStackTrace();
        return CommonResult.failed(HttpStatus.NOT_FOUND,exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<Void> exception(Exception exception) {
        exception.printStackTrace();
        return CommonResult.failed(exception.getMessage());
    }

}
