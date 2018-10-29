package com.xmw.reactive;

import java.util.stream.Stream;

/**
 * @author xmw.
 * @date 2018/9/28 22:23.
 */
public class StreamDemo {
    public static void main(String[] args) {
        // 是不是非常直观
        Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .filter(v -> v % 2 == 0)  // 数值判断 -> 偶数
                .map(v -> v + 1) // 偶数变奇数
                .reduce(Integer::sum)
                .ifPresent(System.out::println);
        //.forEach(System.out::println);
    }
}
