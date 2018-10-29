package com.xmw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xmw.
 * @date 2018/9/27 21:03.
 */
@Controller
public class CacheRestController {

    @RequestMapping("/hello")
    @ResponseBody  // 没有缓存 -> 304  [HttpMessageConverter]
    // 服务端和客户端没有形成默契(状态码)
    // HTTP 协议
    public String helloWorld() {  //200or500or400
        return "hello world...";
    }

    @RequestMapping("/cache")
    public ResponseEntity<String> cacheHelloWorld(
            @RequestParam(required = false, defaultValue = "false") boolean cached) {
        if (cached) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
//        return new ResponseEntity<>("cache hello world", HttpStatus.OK);
        return ResponseEntity.ok("cache hello world");
    }
}
