package com.xmw.spring.cloud.client.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xmw.
 * @date 2018/10/11 22:11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RestClientsRegistrar.class)
public @interface EnableRestClient {

    /**
     * 指定@RestClient接口
     */
    Class<?>[] clients() default {};
}
