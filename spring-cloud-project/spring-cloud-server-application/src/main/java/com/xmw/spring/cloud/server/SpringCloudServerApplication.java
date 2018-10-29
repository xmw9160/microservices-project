package com.xmw.spring.cloud.server;

import com.xmw.spring.cloud.server.stream.SimpleMessageReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Indexed;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * @author xmw.
 * @date 2018/10/8 21:37.
 */
@SpringBootApplication  // 标准spring boot 应用
@EnableDiscoveryClient  // 激活服务发现客户端
@EnableHystrix          // 激活Hystrix
@EnableAspectJAutoProxy(proxyTargetClass = true) // 激活aop
@EnableBinding(SimpleMessageReceiver.class) // 引入并激活SimpleMessageReceiver
@EnableAsync  // 支持异步
@Indexed
public class SpringCloudServerApplication {
    @Autowired
    private SimpleMessageReceiver simpleMessageReceiver;

    public static void main(String[] args) {
        //SpringApplication.run(SpringCloudClientApplication.class, args);
        new SpringApplicationBuilder(SpringCloudServerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    @PostConstruct
    public void init() {
        SubscribableChannel subscribableChannel = simpleMessageReceiver.xmw();
        subscribableChannel.subscribe(message -> {
            MessageHeaders headers = message.getHeaders();
            String encoding = (String) headers.get("charset-encoding");
            String text = (String) headers.get("content-type");
            byte[] content = (byte[]) message.getPayload();
            try {
                System.out.println("接收到消息: " + new String(content, encoding));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    @StreamListener("xmw2018")
    public void onMessage(byte[] data) { // 注解编程
        System.out.println("onMessage byte[] : " + data);
    }

    @StreamListener("xmw2018")
    public void onMessage(String data) { // 注解编程
        System.out.println("onMessage String : " + data);
    }

    @StreamListener("xmw2018")
    public void onMessage2(String data2) { // 注解编程
        System.out.println("onMessage String2 : " + data2);
    }

    @ServiceActivator(inputChannel = "xmw2018")
    public void onServiceActivator(String data) {
        System.out.println("onMessage onServiceActivator : " + data);
    }

    @StreamListener("test007")
    public void onMessageFromRocketMQ(byte[] data) { // 注解编程
        System.out.println("RocketMQ onMessage byte[] : " + data);
    }
}
