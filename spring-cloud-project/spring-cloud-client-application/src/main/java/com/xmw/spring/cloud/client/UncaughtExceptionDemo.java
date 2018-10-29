package com.xmw.spring.cloud.client;

/**
 * @author xmw.
 * @date 2018/10/17 22:13.
 */
public class UncaughtExceptionDemo {
    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            System.out.println(e.getMessage());
        });
        throw new RuntimeException("test....");
    }
}
