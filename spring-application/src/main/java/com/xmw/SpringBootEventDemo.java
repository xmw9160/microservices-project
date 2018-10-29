package com.xmw;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author xmw.
 * @date 2018/9/25 22:36.
 */
@EnableAutoConfiguration
public class SpringBootEventDemo {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootEventDemo.class)
                // 增加监听器
                .listeners(event -> {
                    System.err.println("监听到事件: " + event.getClass().getSimpleName());
                })
                // 运行
                .run(args)
                // 关闭
                .close();
    }
}
