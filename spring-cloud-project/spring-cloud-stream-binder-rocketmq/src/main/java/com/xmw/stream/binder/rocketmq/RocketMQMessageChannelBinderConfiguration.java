package com.xmw.stream.binder.rocketmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQMessageChannelBinderConfiguration {
    @Bean
    public RocketMQMessageChannelBinder rocketMQMessageChannelBinder() {
        return new RocketMQMessageChannelBinder();
    }
}
