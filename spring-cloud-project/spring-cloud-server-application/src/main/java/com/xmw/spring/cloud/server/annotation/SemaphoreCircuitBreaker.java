package com.xmw.spring.cloud.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xmw.
 * @date 2018/10/9 23:13.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)  //运行时保存注解信息
public @interface SemaphoreCircuitBreaker {

    /**
     * 信号量
     * 超时时间
     */
    int value() default -1;
}
