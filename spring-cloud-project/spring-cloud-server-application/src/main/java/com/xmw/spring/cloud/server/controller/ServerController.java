package com.xmw.spring.cloud.server.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xmw.spring.cloud.server.annotation.SemaphoreCircuitBreaker;
import com.xmw.spring.cloud.server.annotation.TimeOutCircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xmw.
 * @date 2018/10/8 21:40.
 */
@RestController
public class ServerController {

    private static final Random RANDOM = new Random();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Value("${spring.application.name}")
    private String currentServiceName;

    public String errorContent(String message) {
        return "request Fault";
    }

    @HystrixCommand(
            fallbackMethod = "errorContent",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "100")
            }
    )
    @GetMapping("/say")
    public String say(@RequestParam String message) throws Exception {
        // 如果随机时间大于100, 那么触发容错
        int value = RANDOM.nextInt(200);
        System.out.println("say() costs " + value + " ms");
        TimeUnit.MILLISECONDS.sleep(value);

        System.out.println("ServerController 接收到消息: " + message);
        return "hello: " + message;
    }

    /**
     * 简易版本 -> 简易容错型
     */
    @GetMapping("/say2")
    public String say2(@RequestParam String message) {
        Future<String> future = executorService.submit(() -> {
            return doSay2(message);
        });
        // 100ms 超时
        String returnValue = null;
        try {
            returnValue = future.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // 超级容错 = 执行错误 或 超时
            returnValue = errorContent(message);
        }
        return returnValue;
    }

    /**
     * 中级版本
     */
    @GetMapping("/middle/say")
    public String middleSay(@RequestParam String message) throws Exception {
        Future<String> future = executorService.submit(() -> doSay2(message));
        // 100ms 超时
        String returnValue = null;
        try {
            returnValue = future.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // 取消执行
            future.cancel(true);
            // 重抛
            throw e;
        }
        return returnValue;
    }

    /**
     * 高级版本-不带注解
     */
    @GetMapping("/advanced/say")
    public String advancedSay(@RequestParam String message) throws Exception {
        return doSay2(message);
    }

    /**
     * 高级版本-带注解
     */
    @GetMapping("/advanced/say2")
    @TimeOutCircuitBreaker(timeout = 100)
    public String advancedSay2(@RequestParam String message) throws Exception {
        return doSay2(message);
    }

    /**
     * 高级版本-注解(信号量)
     */
    @GetMapping("/semaphore/say3")
    @SemaphoreCircuitBreaker(5)
    public String semaphoreSay3(@RequestParam String message) throws Exception {
        return doSay2(message);
    }

    private String doSay2(String message) throws Exception {
        // 如果随机时间大于100, 那么触发容错
        int value = RANDOM.nextInt(200);
        System.out.println("say2() costs " + value + " ms");
        TimeUnit.MILLISECONDS.sleep(value);
        String returnValue = "say2 " + message;
        System.out.println(returnValue);
        return returnValue;
    }
}
