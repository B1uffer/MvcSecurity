package com.b1uffer.securitymvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class nonceController {

    @GetMapping("/test")
    public String test() {
        return "example";
    }
}
