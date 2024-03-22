package com.foticc.security.annotation;

import java.lang.annotation.*;

/**
 * 内部服务调用
 * @see com.foticc.security.annotation.support.InnerApiHandler
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAPI {
}
