package com.xmw.servlet.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xmw.
 * @date 2018/10/15 21:29.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class SpringCloudServletGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServletGatewayApplication.class, args);
    }
}
