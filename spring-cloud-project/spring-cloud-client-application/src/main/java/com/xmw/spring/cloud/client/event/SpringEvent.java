package com.xmw.spring.cloud.client.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author xmw.
 * @date 2018/10/20 10:44.
 */
public class SpringEvent {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 增加ContextRefreshEvent 监听
//        context.addApplicationListener((ApplicationListener<ContextRefreshedEvent>) e ->{
//            System.err.println("ContextRefreshEvent 监听");
//        });
//
//        // 增加ContextClosedEvent监听
//        context.addApplicationListener((ApplicationListener<ContextClosedEvent>) e ->{
//            System.err.println("ContextClosedEvent监听");
//        });

        context.addApplicationListener(e -> {
            System.err.println("监听: " + e.getClass().getSimpleName());
        });

        context.refresh();
        context.start();
        context.stop();
        context.close();
    }
}
