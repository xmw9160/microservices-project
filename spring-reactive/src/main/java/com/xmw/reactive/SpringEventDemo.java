package com.xmw.reactive;

import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xmw.
 * @date 2018/9/28 20:42.
 */
public class SpringEventDemo {
    public static void main(String[] args) {
//        GenericApplicationContext context = new GenericApplicationContext();
//        context.addApplicationListener(event -> {
//            System.out.printf("[线程: %s] event: %s\n", Thread.currentThread().getName(), event);
//        });
//        context.refresh();  //启动
//
//        context.publishEvent("hello world!");
//        context.close();// 关闭

        // 默认是同步非阻塞
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();

        // 切换成异步非阻塞
        ExecutorService service = Executors.newSingleThreadExecutor();
        multicaster.setTaskExecutor(service);

        // 增加事件监听器
        multicaster.addApplicationListener(event -> {
            // 事件监听时
            System.out.printf("[线程: %s] event: %s\n", Thread.currentThread().getName(), event);
        });

        // 广播事件
        multicaster.multicastEvent(new PayloadApplicationEvent<>("Hello world", "Hello World"));

        // 关闭线程池
        service.shutdown();
    }
}
