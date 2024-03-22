package com.foticc.security.annotation;

import com.foticc.security.config.AuthResourceServerConfig;
import com.foticc.security.config.CustomResourceConfig;
import com.foticc.security.config.feign.FeignClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({
        CustomResourceConfig.class,
        AuthResourceServerConfig.class,
        FeignClientConfiguration.class
})
public @interface EnableResourceServer {
}
