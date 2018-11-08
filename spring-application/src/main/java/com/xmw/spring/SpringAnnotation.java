package com.xmw.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author xmw.
 * @date 2018/9/19 22:36.
 */
@Configuration
public class SpringAnnotation {

    public static void main(String[] args) {
        //xml配置文件驱动 ClassPathXmlApplicationContext
        //Annotation驱动 AnnotationConfigApplicationContext
        //找BeanDefinition
        //@Bean @Configuration
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注入
        context.register(SpringAnnotation.class);
        // 启动上下文
        context.refresh();
        // 没有加@Configuration com.xmw.spring.SpringAnnotation@32464a14
        // 加@Configuration com.xmw.spring.SpringAnnotation$$EnhancerBySpringCGLIB$$c09d6f0c@15b204a1
        System.out.println(context.getBean(SpringAnnotation.class));
    }
}
