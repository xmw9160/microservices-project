package com.xmw.spring.cloud.client;

import com.xmw.spring.cloud.client.annotation.EnableRestClient;
import com.xmw.spring.cloud.client.event.HttpRemoteAppEventListener;
import com.xmw.spring.cloud.client.service.feign.clients.SayingService;
import com.xmw.spring.cloud.client.service.rest.clients.RestSayingService;
import com.xmw.spring.cloud.client.stream.SimpleMessageService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xmw.
 * @date 2018/10/8 21:37.
 */
@SpringBootApplication  //标准spring boot 应用
@EnableDiscoveryClient  // 激活服务发现客户端
@EnableScheduling
@EnableFeignClients(clients = SayingService.class)
@EnableRestClient(clients = RestSayingService.class)  // 引入RestClient
@EnableBinding(SimpleMessageService.class) // 激活并引入SimpleMessageService
//@EnableAsync
public class SpringCloudClientApplication {
    public static void main(String[] args) {
        //SpringApplication.run(SpringCloudClientApplication.class, args);
        new SpringApplicationBuilder(SpringCloudClientApplication.class)
                .web(WebApplicationType.SERVLET)
                .listeners(new HttpRemoteAppEventListener())
                .run(args);
    }
}
