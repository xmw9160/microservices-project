package com.xmw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author xmw.
 * @date 2018/9/26 21:12.
 */
@Controller
public class IndexController {

    @GetMapping(value = {"/", ""})
    public String index(Model model) {
//        model.addAttribute("message", "hello world");
        return "index";
    }


    @ModelAttribute(name = "message")
    public String message() {
        return "hello world...";
    }
}
