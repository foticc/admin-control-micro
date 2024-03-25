package com.foticc.security.config;

import com.foitcc.common.spirng.SpringUtils;
import com.foticc.security.annotation.InnerAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 容易启动时
 * 将标有InnerAPI注解的Mapping 放入到permitUrl
 * security permitAll
 */
@ConditionalOnProperty(prefix = "security.oauth2.resource.ignores" )
@ConfigurationProperties(prefix = "security.oauth2.resource.ignores")
public class PermitUrlProperties implements InitializingBean{

    private final static Logger log = LoggerFactory.getLogger(PermitUrlProperties.class);

    private List<String> urls = new ArrayList<>();

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = handlerMethods.entrySet();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : entries) {
            HandlerMethod method = entry.getValue();
            InnerAPI methodAnnotation = method.getMethodAnnotation(InnerAPI.class);
            if (methodAnnotation!=null) {
                RequestMappingInfo mappingInfo = entry.getKey();
                PathPatternsRequestCondition pathPatternsCondition = mappingInfo.getPathPatternsCondition();
                if (pathPatternsCondition!=null && !pathPatternsCondition.isEmptyPathMapping()) {
                    urls.addAll(pathPatternsCondition.getPatternValues());
                }
            }
        }

        urls.forEach(f->{
            log.info("permitUrl-{}",f);
        });
    }
}
