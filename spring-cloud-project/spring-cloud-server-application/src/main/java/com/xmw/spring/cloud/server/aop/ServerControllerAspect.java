package com.xmw.spring.cloud.server.aop;

import com.xmw.spring.cloud.server.annotation.SemaphoreCircuitBreaker;
import com.xmw.spring.cloud.server.annotation.TimeOutCircuitBreaker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * {@link com.xmw.spring.cloud.server.controller.ServerController}
 *
 * @author xmw.
 * @date 2018/10/9 22:20.
 */
@Aspect
@Component
public class ServerControllerAspect {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(20);

    private Semaphore semaphore;

    @Around("execution(* com.xmw.spring.cloud.server.controller.ServerController.advancedSay(..)) && args(message)")
    public Object advancedSayTimeout(ProceedingJoinPoint joinPoint, String message) throws Throwable {
        return doInvoke(joinPoint, message, 100);
    }

//    @Around("execution(* com.xmw.spring.cloud.server.controller.ServerController.advancedSay2(..))" +
//            " && args(message) && @annotation(circuitBreaker)")
////    @Around("@annotation(com.xmw.spring.cloud.server.annotation.CircuitBreaker)")
//    public Object advancedSay2Timeout(ProceedingJoinPoint joinPoint,
//                                      String message, CircuitBreaker circuitBreaker) throws Throwable {
//        // 获取超时时间
//        long timeout = circuitBreaker.timeout();
//        return doInvoke(joinPoint, message, timeout);
//    }

    @Around("execution(* com.xmw.spring.cloud.server.controller.ServerController.advancedSay2(..))" +
            " && args(message)")
//    @Around("@annotation(com.xmw.spring.cloud.server.annotation.CircuitBreaker)")
    public Object advancedSay2Timeout(ProceedingJoinPoint joinPoint,
                                      String message) throws Throwable {
        long timeout = 100;
        if (joinPoint instanceof MethodInvocationProceedingJoinPoint) {
            MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
            MethodSignature signature = (MethodSignature) methodPoint.getSignature();
            Method method = signature.getMethod();
            TimeOutCircuitBreaker circuitBreaker = method.getAnnotation(TimeOutCircuitBreaker.class);
            timeout = circuitBreaker.timeout();
        }
        return doInvoke(joinPoint, message, timeout);
    }

    @Around("execution(* com.xmw.spring.cloud.server.controller.ServerController.advancedSay3(..))" +
            " && args(message) && @annotation(circuitBreaker)")
//    @Around("@annotation(com.xmw.spring.cloud.server.annotation.CircuitBreaker)")
    public Object advancedSay3Semaphore(ProceedingJoinPoint joinPoint,
                                        String message,
                                        SemaphoreCircuitBreaker circuitBreaker) throws Throwable {
        int value = circuitBreaker.value();
        if (semaphore == null) {
            semaphore = new Semaphore(value);
        }
        Object returnValue = null;
        try {
            if (semaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                returnValue = joinPoint.proceed(new Object[]{message});
            } else {
                returnValue = errorContent(message);
            }
        } finally {
            semaphore.release();
        }

        return returnValue;
    }

    private Object doInvoke(ProceedingJoinPoint joinPoint,
                            String message, long timeout) throws Throwable {
        Future<Object> future = executorService.submit(() -> {
            Object returnValue = null;
            try {
                returnValue = joinPoint.proceed(new Object[]{message});
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return returnValue;
        });
        // 超时处理
        Object returnValue;
        try {
            returnValue = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // 取消执行
            future.cancel(true);
            returnValue = errorContent("");
        }
        return returnValue;
    }

    private String errorContent(String message) {
        return "request Fault ServerControllerAspect";
    }

    @PreDestroy
    public void preDestroy() {
        executorService.shutdown();
    }
}
