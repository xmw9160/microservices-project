package com.xmw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xmw.
 * @date 2018/10/7 15:45.
 */
@SpringBootApplication
@EnableDiscoveryClient  //尽可能使用@EnableDiscoveryClient
public class ZKDSClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZKDSClientApplication.class, args);
    }
}
