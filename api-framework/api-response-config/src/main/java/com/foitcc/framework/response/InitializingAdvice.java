package com.foitcc.framework.response;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
// todo 测试
public class InitializingAdvice implements InitializingBean {


    private final RequestMappingHandlerAdapter adapter;

    public InitializingAdvice(RequestMappingHandlerAdapter adapter) {
        this.adapter = adapter;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(returnValueHandlers);
        doInsertHandler(handlers);
        adapter.setReturnValueHandlers(handlers);
    }

    /**
     * 自定义RequestResponseBodyMethodProcessor
     * @param handlers
     */
    private void doInsertHandler(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler returnValueHandler : handlers) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                ResponseBodyHandler responseBodyHandler =
                        new ResponseBodyHandler((RequestResponseBodyMethodProcessor) returnValueHandler);
                int index = handlers.indexOf(returnValueHandler);
                handlers.set(index, responseBodyHandler);
                break;
            }
        }
    }
}
