package com.xmw.reactive.webflux;

import com.xmw.util.PrintUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmw.
 * @date 2018/11/10 21:16.
 */
@RestController
public class TestController {

    @GetMapping(value = "/test")
    public String test() {
        PrintUtil.println("执行请求");
        return "simple request.";
    }
}
