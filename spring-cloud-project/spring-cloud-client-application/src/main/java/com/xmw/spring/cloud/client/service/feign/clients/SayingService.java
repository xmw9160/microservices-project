package com.xmw.spring.cloud.client.service.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xmw.
 * @date 2018/10/11 21:36.
 */
@FeignClient(name = "spring-cloud-server-application")
public interface SayingService {

    @GetMapping("/say")
    String say(@RequestParam(name = "message") String message);
}
