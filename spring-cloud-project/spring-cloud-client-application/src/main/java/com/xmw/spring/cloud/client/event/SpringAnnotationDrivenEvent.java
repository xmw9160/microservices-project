package com.xmw.spring.cloud.client.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * @author xmw.
 * @date 2018/10/20 11:00.
 */
public class SpringAnnotationDrivenEvent {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // SpringAnnotationDrivenEvent注册为Spring Bean
        context.register(SpringAnnotationDrivenEvent.class);

        context.refresh(); // 启动上下文
        // 确保上下文启动完毕后, 再发送事件
        context.publishEvent(new MyApplicationEvent("hello, world"));
        context.close(); // 关闭上下文
    }

    @EventListener
    public void onMessage(MyApplicationEvent event) {
        System.err.println("监听到MyApplicationEvent事件: " + event.getSource());
    }

    @EventListener
    public void onMessage(String eventSource) {
        System.err.println("监听到MyApplicationEvent事件源: " + eventSource);
    }

    private static class MyApplicationEvent extends ApplicationEvent {

        /**
         * Create a new ApplicationEvent.
         *
         * @param source the object on which the event initially occurred (never {@code null})
         */
        public MyApplicationEvent(Object source) {
            super(source);
        }
    }
}
