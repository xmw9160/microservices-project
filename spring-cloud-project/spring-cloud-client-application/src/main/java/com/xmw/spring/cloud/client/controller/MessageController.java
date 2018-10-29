package com.xmw.spring.cloud.client.controller;

import com.xmw.spring.cloud.client.stream.SimpleMessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xmw.
 * @date 2018/10/18 20:16.
 */
@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SimpleMessageService simpleMessageService;

    @GetMapping("send")
    public String send(@RequestParam("message") String message) {
        rabbitTemplate.convertAndSend(message);
        return "OK";
    }

    @GetMapping("stream/send")
    public boolean streamSend(@RequestParam("message") String message) {
        // 获取MessageChannel
        MessageChannel messageChannel = simpleMessageService.xmw();
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("charset-encoding", "UTF-8");
        headMap.put("content-type", MediaType.TEXT_PLAIN.toString());
        GenericMessage<String> msg = new GenericMessage<>(message, headMap);
        return messageChannel.send(msg);
    }

    @GetMapping("stream/send/rocketmq")
    public boolean streamSendRocketmq(@RequestParam("message") String message) {
        // 获取MessageChannel
        MessageChannel messageChannel = simpleMessageService.testChannel();
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("charset-encoding", "UTF-8");
        headMap.put("content-type", MediaType.TEXT_PLAIN.toString());
        GenericMessage<String> msg = new GenericMessage<>(message, headMap);
        return messageChannel.send(msg);
    }
}
