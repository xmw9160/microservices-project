package com.xmw.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xmw.
 * @date 2018/10/6 22:22.
 */
@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServer.class, args);
    }

    @Bean
    public EnvironmentRepository environmentRepository() {
        return (application, profile, label) -> {
            Environment environment = new Environment("default", profile);
            List<PropertySource> propertySources = environment.getPropertySources();
            Map<String, Object> source = new HashMap<>();
            source.put("name", "夏明尉");
            PropertySource propertySource = new PropertySource("map", source);
            // 追加PropertySource
            propertySources.add(propertySource);
            return environment;
        };
    }
}
