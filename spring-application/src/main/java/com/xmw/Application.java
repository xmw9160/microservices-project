package com.xmw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);

//        new SpringApplicationBuilder(Application.class)
//                // 单元测试是PORT=RANDOM
//                .properties("server.port=0")
//                .run(args);

        SpringApplication springApplication = new SpringApplication();
////        SpringApplication springApplication = new SpringApplication(Application.class);
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("server.port", 8080);
        springApplication.setDefaultProperties(properties);
        // 设置为非web应用程序
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.addPrimarySources(Arrays.asList(Application.class));
        ConfigurableApplicationContext context = springApplication.run(args);

        System.out.println(context.getBean(Application.class));
        // 输出当前Spring Boot应用的ApplicationContext类名
        //WebApplicationType.NONE org.springframework.context.annotation.AnnotationConfigApplicationContext
        //Web项目: org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
        System.out.println("当前Spring应用上下文的类: " + context.getClass().getName());
    }
}
