package com.foticc.security.annotation.support;

import com.foitcc.common.security.SecurityConstants;
import com.foticc.security.annotation.InnerAPI;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;

/**
 *
 */
@Aspect
public class InnerApiHandler implements Ordered {

    private final HttpServletRequest request;

    public InnerApiHandler(HttpServletRequest request) {
        this.request = request;
    }


    @Before("@annotation(api)")
    public void before(JoinPoint joinPoint, InnerAPI api) {
        if (api == null) {
            Class<?> clazz = joinPoint.getTarget().getClass();
            api = AnnotationUtils.findAnnotation(clazz, InnerAPI.class);
        }
        if (api!=null) {
            String header = request.getHeader(SecurityConstants.HEADER_FROM);
            if (!StringUtils.hasText(header) || !SecurityConstants.HEADER_FROM_INNER.equalsIgnoreCase(header)) {
                throw new AccessDeniedException("Access is denied");
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
