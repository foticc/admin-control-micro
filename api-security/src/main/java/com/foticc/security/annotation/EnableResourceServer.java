package com.foticc.security.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableResourceServer {
}
