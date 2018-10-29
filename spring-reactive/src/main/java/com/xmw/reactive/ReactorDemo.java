package com.xmw.reactive;

import com.xmw.util.PrintUtil;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author xmw.
 * @date 2018/9/28 22:50.
 */
public class ReactorDemo {
    public static void main(String[] args) throws InterruptedException {
        Flux.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) // 直接执行
                .filter(v -> v % 2 == 0)  // 数值判断 -> 偶数
                .map(v -> v + 1) // 偶数变奇数
                .reduce(Integer::sum)
                .subscribeOn(Schedulers.parallel())
                .subscribe(PrintUtil::println);

        TimeUnit.SECONDS.sleep(1);

//        PrintUtil.println(Flux.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) // 直接执行
//                .filter(v -> v % 2 == 0)  // 数值判断 -> 偶数
//                .map(v -> v + 1) // 偶数变奇数
//                .reduce(Integer::sum)
//                .subscribeOn(Schedulers.elastic())
//                .block());
    }
}
