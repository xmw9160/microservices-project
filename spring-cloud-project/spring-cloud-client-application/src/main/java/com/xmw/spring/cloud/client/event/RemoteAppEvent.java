package com.xmw.spring.cloud.client.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author xmw.
 * @date 2018/10/20 14:04.
 */
public class RemoteAppEvent extends ApplicationEvent {
    /**
     * 应用名称
     */
    private final String appName;

    /**
     * 发送者
     */
//    private String sender;
    /**
     * 时候广播到集群
     */
    private final boolean isCluster;

    /**
     * 应用实例
     */
//    private List<ServiceInstance> serviceInstances;
    /**
     * 事件传输类型HTTP, RPC, MQ
     */
    private String type;

    /**
     * @param source    POJO 事件源, JOSN格式
     * @param appName   appName
     * @param isCluster 集群
     */
    public RemoteAppEvent(Object source, String appName, boolean isCluster) {
        super(source);
        this.appName = appName;
        this.isCluster = isCluster;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppName() {
        return appName;
    }
}
