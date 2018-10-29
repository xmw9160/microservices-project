package com.xmw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xmw.
 * @date 2018/10/7 16:34.
 */
@RestController
public class ServiceController {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 返回所有的服务名称
     */
    @GetMapping("/services")
    public List<String> getAllService() {
        return discoveryClient.getServices();
    }

    @GetMapping("/service/instance/{serviceName}")
    public List<String> getAllServiceInstances(@PathVariable("serviceName") String serviceName) {
        return discoveryClient.getInstances(serviceName).stream()
                .map(s -> s.getServiceId() + "-" + s.getHost() + ":" + s.getPort()).collect(Collectors.toList());
    }
}
