package com.wixsite.mupbam1.b_hello_world.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestHeader(value = "X-User-Name", defaultValue = "Guest") String username, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("firstLetter", username.substring(0, 1).toUpperCase());
        return "hello";
    }
}
