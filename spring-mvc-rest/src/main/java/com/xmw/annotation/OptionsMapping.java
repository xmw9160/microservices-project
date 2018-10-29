package com.xmw.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xmw.
 * @date 2018/9/27 22:14.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.OPTIONS)  // 如果不增加元注解, 会报错
public @interface OptionsMapping {
    // 需要重新定义属性
    @AliasFor(annotation = RequestMapping.class)  // 指定之后， RequestMethod的属性
            String name() default ""; // 不加@AliasFor的话，只是代表自己
}
