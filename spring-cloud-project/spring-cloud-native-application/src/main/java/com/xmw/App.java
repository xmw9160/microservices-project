package com.xmw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 */
@SpringBootApplication
@RestController
public class App {
    @Autowired  //String message Bean
    @Qualifier(value = "helloworld") // Bean名称,来自于"xmw"上下文
    private String message;

    public static void main(String[] args) {
        // SpringApplication.run(App.class, args);

        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.setId("xmw");

        // 在"xmw"注册一个"helloworld" String类型的bean
        parentContext.registerBean("helloworld", String.class, "hello world");
        // 启动"xmw" 上下文
        parentContext.refresh();

        // 类比于 Spring WebMVC, Root WebApplication和DispatcherServlet WebApplication
        // DispatcherServlet WebApplication parent = WebApplication

        new SpringApplicationBuilder(App.class)
                .parent(parentContext) // 显式地设置双亲上下文
                .run(args);
    }

    @RequestMapping("")
    public String hello() {
        return message;
    }
}
