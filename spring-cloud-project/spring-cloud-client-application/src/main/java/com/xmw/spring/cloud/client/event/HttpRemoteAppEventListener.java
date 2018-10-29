package com.xmw.spring.cloud.client.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RemoteAppEvent} 监听器, 将事件数据发送Http请求到目标机器
 * 监听{@link org.springframework.context.event.ContextRefreshedEvent}
 *
 * @author xmw.
 * @date 2018/10/20 14:16.
 */
@Slf4j
public class HttpRemoteAppEventListener implements SmartApplicationListener
        /*implements ApplicationListener<RemoteAppEvent>*/ {
    private RestTemplate restTemplate = new RestTemplate();
    // 得到DiscoveryClient Bean
    private DiscoveryClient discoveryClient;

    private String currentAppName;

    public void onApplicationEvent(RemoteAppEvent event) {
        Object source = event.getSource();
        String appName = event.getAppName();
//        String sender = event.getSender();
//        List<ServiceInstance> serviceInstances = event.getServiceInstances();
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(appName);
        log.error("2.监听到发布事件...");
        for (ServiceInstance s : serviceInstances) {
            String remoteURL = (s.isSecure() ? "https://" : "http://") + s.getHost() + ":" + s.getPort();

            String url = remoteURL + "/receive/remote/event";
            Map<String, Object> data = new HashMap<>();
            data.put("sender", currentAppName);
            data.put("value", source);
            data.put("type", RemoteAppEvent.class.getName());
            // 发送HTTP请求
            String responseContent = restTemplate.postForObject(url, data, String.class);
            System.out.println(responseContent);
        }
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return RemoteAppEvent.class.isAssignableFrom(eventType)
                || ContextRefreshedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof RemoteAppEvent) {
            onApplicationEvent((RemoteAppEvent) event);
        } else if (event instanceof ContextRefreshedEvent) {
            onContextRefreshedEvent((ContextRefreshedEvent) event);
        }
    }

    private void onContextRefreshedEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        this.discoveryClient = context.getBean(DiscoveryClient.class);
        this.currentAppName = context.getEnvironment().getProperty("spring.application.name");
        System.out.println("currentAppName: " + context.getApplicationName());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
