package com.foitcc.common.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class CommonResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    protected CommonResult() {
    }


    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static CommonResult<Void> success() {
        return success(null);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static CommonResult<Void> failed() {
        return failed(null);
    }

    public static CommonResult<Void> failed(String msg) {
        return new CommonResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static CommonResult<Void> failed(HttpStatus error, String msg) {
        return new CommonResult<>(error.value(), msg, null);
    }

}
