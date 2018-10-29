package com.xmw.spring.cloud.client.controller;

import com.xmw.spring.cloud.client.annotation.CustomizedLoadBalanced;
import com.xmw.spring.cloud.client.loadbalance.LoadBalanceRequestInterceptor;
import com.xmw.spring.cloud.client.service.feign.clients.SayingService;
import com.xmw.spring.cloud.client.service.rest.clients.RestSayingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xmw.
 * @date 2018/10/8 21:40.
 */
@RestController
public class ClientController {

    // 依赖注入自定义RestTemplate
    @Autowired
    @CustomizedLoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    @LoadBalanced // 依赖注入Ribbon RestTemplate
    private RestTemplate lbRestTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String currentServiceName;

    @Autowired
    private SayingService sayingService;

    @Autowired
    private RestSayingService restSayingService;

    // 线程安全
//    private volatile Set<String> targetUrls = new HashSet<>();

    // Map key service name, value URLs
    private volatile Map<String, Set<String>> targetUrlsCache = new HashMap<>();

    // 更新目标URls
//    @Scheduled(fixedRate = 10 * 1000) //10秒钟更新一次缓存
//    public void updateInvocationUrls() {
//        // 获取当前应用的机器列表
//        // http://${ip}:${port}
//        List<ServiceInstance> instances = discoveryClient.getInstances(currentServiceName);
//        Set<String> oldTargetUrls = this.targetUrls;
//        Set<String> newTargetUrls = instances.stream()
//                .map(s -> (s.isSecure() ? "https://" : "http://") + s.getHost() + ":" + s.getPort())
//                .collect(Collectors.toSet());
//        // swap
//        this.targetUrls = newTargetUrls;
//        oldTargetUrls.clear(); // help gc
//    }

//    @Scheduled(fixedRate = 10 * 1000) //10秒钟更新一次缓存
//    public void updateInvocationTargetUrlsCache() {
//        // 获取当前应用的机器列表
//        // http://${ip}:${port}
//        Map<String, Set<String>> oldTargetUrlsCache = this.targetUrlsCache;
//        Map<String, Set<String>> newTargetUrlsCache = new HashMap<>();
//        discoveryClient.getServices().forEach(serverName -> {
//            List<ServiceInstance> instances = discoveryClient.getInstances(serverName);
//            Set<String> newTargetUrls = instances.stream()
//                    .map(s -> (s.isSecure() ? "https://" : "http://") + s.getHost() + ":" + s.getPort())
//                    .collect(Collectors.toSet());
//            newTargetUrlsCache.put(serverName, newTargetUrls);
//        });
//
//        // swap
//        this.targetUrlsCache = newTargetUrlsCache;
//        oldTargetUrlsCache.clear(); // help gc
//    }

//    @GetMapping("/invoke/say")  //  ->say
//    public String invokeSay(@RequestParam String message) {
//        // 服务器列表快照, 避免方法多次获取的结果不一致
//        List<String> targetUrls = new ArrayList<>(this.targetUrls);
//        int size = targetUrls.size();
//        // size=3, index=0-2
//        int index = new Random().nextInt(size);
//        // 选择其中一台服务器
//        String targetUrl = targetUrls.get(index);
//        // ResTemplate 发送请求到服务器
//        return restTemplate.getForObject(targetUrl + "/say?message=" + message, String.class);
//    }

    @GetMapping("/invoke/{serviceName}/say")  //  ->say
    public String invokeSay(@PathVariable String serviceName, @RequestParam String message) {
//        // 服务器列表快照, 避免方法多次获取的结果不一致
//        List<String> targetUrls = new ArrayList<>(targetUrlsCache.get(serviceName));
//        int size = targetUrls.size();
//        // size=3, index=0-2
//        int index = new Random().nextInt(size);
//        // 选择其中一台服务器
//        String targetUrl = targetUrls.get(index);
        // 自定义ResTemplate 发送请求到服务器
//        return restTemplate.getForObject(targetUrl + "/say?message=" + message, String.class);
        return restTemplate.getForObject("/" + serviceName + "/say?message=" + message, String.class);
    }

    @GetMapping("/lb/invoke/{serviceName}/say")  //  ->say
    public String lbInvokeSay(@PathVariable String serviceName, @RequestParam String message) {
        // Ribbon RestTemplate 发送请求
        return lbRestTemplate.getForObject("Http://" + serviceName + "/say?message=" + message, String.class);
    }

    // 使用feign 请求
    @GetMapping("/feign/say")
    public String sayService(@RequestParam String message) {
        return sayingService.say(message);
    }

    // 使用feign 请求
    @GetMapping("/rest/say")
    public String restSayService(@RequestParam String message) {
        return restSayingService.say(message);
    }

    @Bean
    public ClientHttpRequestInterceptor interceptor() {
        return new LoadBalanceRequestInterceptor();
    }

    // Ribbon RestTemplate
    @Bean
    @LoadBalanced
    public RestTemplate loadBalanceRestTemplate() {
        return new RestTemplate();
    }

    // 自定义RestTemplate Bean
//    @Bean
//    @Autowired
//    private RestTemplate restTemplate(ClientHttpRequestInterceptor interceptor) {
//        RestTemplate restTemplate = new RestTemplate();
//        // 增加拦截器
//        restTemplate.setInterceptors(Collections.singletonList(interceptor));
//        return restTemplate;
//    }

    @Bean
    @Autowired
    @CustomizedLoadBalanced
    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Autowired
    public Object customizer(@CustomizedLoadBalanced Collection<RestTemplate> restTemplates,
                             ClientHttpRequestInterceptor interceptor) {
        restTemplates.forEach(r -> r.setInterceptors(Collections.singletonList(interceptor)));
        return new Object();
    }
}
