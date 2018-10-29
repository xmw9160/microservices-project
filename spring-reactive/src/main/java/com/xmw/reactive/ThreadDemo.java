package com.xmw.reactive;

/**
 * @author xmw.
 * @date 2018/9/28 21:50.
 */
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        println("Hello World");

        Thread thread = new Thread(() -> {
            // 线程任务
            println("hello world 2018");
        });
        // 线程名称
        thread.setName("sub-thread");
        thread.start();

        // 线程join 等待线程销毁
        thread.join();

        println("Hello World 2");
    }

    public static void println(Object value) {
        System.out.printf("[线程: %s] event: %s\n", Thread.currentThread().getName(), value);
    }
}
