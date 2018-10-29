package com.xmw.spring.cloud.server.web.mvc;

import com.xmw.spring.cloud.server.controller.ServerController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeoutException;

/**
 * @author xmw.
 * @date 2018/10/9 21:41.
 */
@RestControllerAdvice(assignableTypes = ServerController.class)
public class CircuitBreakerControllerAdvice {

    @ExceptionHandler
    public void onTimeoutException(TimeoutException timeoutException, Writer writer) throws IOException {
        writer.write(errorContent(""));
    }

    private String errorContent(String message) {
        return "request Fault CircuitBreakerControllerAdvice";
    }
}
