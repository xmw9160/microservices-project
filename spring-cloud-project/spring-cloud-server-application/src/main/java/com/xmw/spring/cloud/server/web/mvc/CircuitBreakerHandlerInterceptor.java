package com.xmw.spring.cloud.server.web.mvc;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xmw.
 * @date 2018/10/9 21:21.
 */
public class CircuitBreakerHandlerInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        if ("/middle/say".equalsIgnoreCase(request.getRequestURI()) && ex instanceof TimeoutException) {
//            // 没返回回去, 报错了
//            PrintWriter writer = response.getWriter();
//             writer.write(errorContent(""));
//        }
    }

    private String errorContent(String message) {
        return "request Fault";
    }
}
