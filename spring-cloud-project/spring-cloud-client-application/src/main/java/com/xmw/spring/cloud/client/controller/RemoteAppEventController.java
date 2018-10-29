package com.xmw.spring.cloud.client.controller;

import com.xmw.spring.cloud.client.event.RemoteAppEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 远程应用事件控制器
 *
 * @author xmw.
 * @date 2018/10/20 11:10.
 */
@Slf4j
@RestController
public class RemoteAppEventController implements ApplicationEventPublisherAware {

    @Value("${spring.application.name}")
    private String currentAppName;

    private ApplicationEventPublisher publisher;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("send/remote/event")
    public String sendEvent(@RequestParam String message) {
        publisher.publishEvent(message);
        return "Sent";
    }

//    @GetMapping("/send/remote/event/{appName}")
//    public String sendAppCluster(@PathVariable String appName, String data) {
//        // 发送数据到自己
//        List<ServiceInstance> instances = discoveryClient.getInstances(appName);
//        RemoteAppEvent remoteAppEvent = new RemoteAppEvent(data, currentAppName, appName, instances);
//        publisher.publishEvent(remoteAppEvent);
//        return "sent...";
//    }

    @PostMapping("/send/remote/event/{appName}")
    public String sendAppCluster(@PathVariable String appName, @RequestBody Object data) {
        // 发送数据到自己
        RemoteAppEvent remoteAppEvent = new RemoteAppEvent(data, appName, true);
        log.error("1.发布事件...");
        publisher.publishEvent(remoteAppEvent);
        return "sent...";
    }

//    @PostMapping("/send/remote/event/{appName}/{ip}/{port}")
//    public String sendAppCluster(@PathVariable String appName,
//                                 @PathVariable String ip, @PathVariable int port,
//                                 @RequestBody Object data) {
//        ServiceInstance serviceInstance = new DefaultServiceInstance(appName, ip, port,false);
//        RemoteAppEvent remoteAppEvent = new RemoteAppEvent(data, currentAppName, appName, Collections.singletonList(serviceInstance));
//        publisher.publishEvent(remoteAppEvent);
//        return "sent...";
//    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
