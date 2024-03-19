package com.foitcc.framework.response;

import com.foitcc.common.model.CommonResult;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

public class ResponseBodyHandler implements HandlerMethodReturnValueHandler {


    private final RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    public ResponseBodyHandler(RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor) {
        this.requestResponseBodyMethodProcessor = requestResponseBodyMethodProcessor;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
//        if(returnValue instanceof Page<?> as){
//            returnValue = new PageResult(as.getTotalElements(),as.getContent());
//        }
        requestResponseBodyMethodProcessor.handleReturnValue(CommonResult.success(returnValue),returnType, mavContainer, webRequest);
    }
}
