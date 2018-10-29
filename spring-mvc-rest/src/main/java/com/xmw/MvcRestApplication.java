package com.xmw;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 */
@ComponentScan(basePackages = "com.xmw.controller")
@EnableAutoConfiguration
public class MvcRestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MvcRestApplication.class).run(args);
    }
}
