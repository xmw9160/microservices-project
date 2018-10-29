package com.xmw.spring.cloud.client.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author xmw.
 * @date 2018/10/18 21:00.
 */
public interface SimpleMessageService {

    @Output("xmw2018") // Channel name
    MessageChannel xmw(); //destination=xmw2018

    @Output("test007") // Channel name
    MessageChannel testChannel(); //destination=test007
}
