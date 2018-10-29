package com.xmw.spring.cloud.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xmw.
 * @date 2018/10/20 14:44.
 */
@Slf4j
@RestController
public class RemoteAppEventReceiverController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @PostMapping("/receive/remote/event")
    public String receive(@RequestBody Map<String, Object> data) {  //REST 请求不需要具体类型
        // 事件的发送者
        String sender = (String) data.get("sender");
        // 事件的数据内容
        Object value = data.get("value");
        // 事件类型
        String type = (String) data.get("type");
        // 接收到对象内容,同样也要发送事件到本地, 做处理
        log.error("3.服务端发布事件...");
        publisher.publishEvent(new SenderRemoteAppEvent(sender, value));
        return "received";
    }

    @EventListener
    @Async
    public void onMessage(SenderRemoteAppEvent event) {
        log.error("4.服务端监听到发布事件...");
        System.out.println("接收到数据: " + event + ", 来自于应用: " + event.getSender());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    private static class SenderRemoteAppEvent extends ApplicationEvent {
        private final String sender;

        private SenderRemoteAppEvent(String sender, Object value) {
            super(value);
            this.sender = sender;
        }

        public String getSender() {
            return sender;
        }
    }
}
