package com.anurag.sms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home(){
        return page1;
    }
}
