package com.xmw.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xmw.
 * @date 2018/9/27 22:38.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Service // 它是Service组件
@Transactional // 它是事务组件
public @interface TransactionalService {  // @Service + @Transactional

    @AliasFor(annotation = Service.class)
    String value(); //服务名称

    @AliasFor(annotation = Transactional.class, attribute = "value")
    String txName();
}
