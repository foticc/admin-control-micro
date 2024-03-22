package com.foticc.security.annotation.support;

import com.foticc.security.annotation.InnerAPI;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;

/**
 *
 */
@Aspect
public class InnerApiHandler implements Ordered {


    @Before("@annotation(api)")
    public void before(JoinPoint joinPoint, InnerAPI api) {
        if (api == null) {
            Class<?> clazz = joinPoint.getTarget().getClass();
            api = AnnotationUtils.findAnnotation(clazz, InnerAPI.class);
        }
        if (api == null) {
            throw new AccessDeniedException("Access is denied");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
