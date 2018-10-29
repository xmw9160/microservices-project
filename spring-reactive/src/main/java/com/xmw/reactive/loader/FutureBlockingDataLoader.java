package com.xmw.reactive.loader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author xmw.
 * @date 2018/9/28 21:12.
 */
public class FutureBlockingDataLoader extends DataLoader {

    public static void main(String[] args) {
        new FutureBlockingDataLoader().load();
    }

    protected void doLoad() {
        ExecutorService executorService = Executors.newFixedThreadPool(3); // 创建线程池
        runCompletely(executorService.submit(super::loadConfigurations));  //  耗时 >= 1s
        runCompletely(executorService.submit(super::loadUsers));           //  耗时 >= 2s
        runCompletely(executorService.submit(super::loadOrders));          //  耗时 >= 3s
        executorService.shutdown();
    } // 总耗时 sum(>= 1s, >= 2s, >= 3s)  >= 6s

    private void runCompletely(Future<?> future) {
        try {
            future.get(); // 阻塞等待结果执行
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
