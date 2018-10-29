package com.xmw.reactive;

import com.xmw.util.PrintUtil;

import java.util.concurrent.CompletableFuture;

/**
 * @author xmw.
 * @date 2018/9/28 21:27.
 */
public class CompletableFutureDemo {
    public static void main(String[] args) {
        PrintUtil.println("当前线程");
        CompletableFuture.supplyAsync(() -> {
            PrintUtil.println("第一步返回hello");
            return "hello";
        }).thenApplyAsync(result -> {  // 异步?
            PrintUtil.println("第一步 + world");
            return result + ", World.";
        }).thenAccept(PrintUtil::println)
                .whenComplete((v, error) -> {
                    PrintUtil.println("执行结束");
                })
                .join();  // 等待执行结束

        // 三段式编程
        // 业务执行
        // 执行完成
        // 异常处理
        try {
            // action
        } catch (Exception e) {
            // error
        } finally {
            // complete
        }

        // Reactive programming
        // fluent   流畅的
        // Streams  流逝的
    }
}
