package com.anurag.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Redirect root URL "/" to the dashboard
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}
