package com.xmw.util;

/**
 * @author xmw.
 * @date 2018/9/28 22:52.
 */
public class PrintUtil {
    public static void println(Object value) {
        System.out.printf("[线程: %s] event: %s\n", Thread.currentThread().getName(), value);
    }
}
