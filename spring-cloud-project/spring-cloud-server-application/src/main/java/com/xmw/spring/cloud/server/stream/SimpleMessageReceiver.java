package com.xmw.spring.cloud.server.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author xmw.
 * @date 2018/10/18 21:16.
 */
public interface SimpleMessageReceiver {

    @Input("xmw2018")
    SubscribableChannel xmw();

    @Input("test007")
    SubscribableChannel testChannel();
}
