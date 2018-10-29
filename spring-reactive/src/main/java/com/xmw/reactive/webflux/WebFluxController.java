package com.xmw.reactive.webflux;

import com.xmw.util.PrintUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author xmw.
 * @date 2018/9/28 23:06.
 */
@RestController
public class WebFluxController {

    @RequestMapping("")
    public Mono<String> index() {
        // 执行计算
        PrintUtil.println("执行计算");
        return Mono.fromSupplier(() -> {
            PrintUtil.println("返回结果");
            return "hello world";
        });
    }
}
