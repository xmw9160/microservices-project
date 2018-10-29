package com.xmw.spring.cloud.client.service.rest.clients;

import com.xmw.spring.cloud.client.annotation.RestClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author xmw.
 * @date 2018/10/11 22:16.
 */
//@RestClient(name = "spring-cloud-server-application")
@RestClient(name = "${saying.rest.service.name}")
public interface RestSayingService {
    public static void main(String[] args) {
        Method method = ReflectionUtils.findMethod(RestSayingService.class, "say", String.class);
        ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

        Stream.of(discoverer.getParameterNames(method)).forEach(System.out::println);
    }

    @GetMapping("/say")
    String say(@RequestParam(value = "message") String message);  // 请求参数与方法参数同名
}
