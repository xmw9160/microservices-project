package com.xmw.spring;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

/**
 * @author xmw.
 * @date 2018/9/25 22:08.
 */
public class ApplicationEventMulticasterDemo {
    public static void main(String[] args) {
        ApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.addApplicationListener(event -> {
            if (event instanceof PayloadApplicationEvent) {
                System.out.println("接收到 PayloadApplicationEvent: " +
                        PayloadApplicationEvent.class.cast(event).getPayload());
            } else {
                System.out.println("接收到事件: " + event);
            }
        });
        // 广播事件
        multicaster.multicastEvent(new PayloadApplicationEvent<Object>("xmw", "hello world"));
        multicaster.multicastEvent(new MyEvent("MyEvent hello world"));
    }

    private static class MyEvent extends ApplicationEvent {
        public MyEvent(Object source) {
            super(source);
        }
    }
}
