package com.wixsite.mupbam1.b_hello_world.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wixsite.mupbam1.b_hello_world.repository.ResourseRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final ResourseRepository resourseRepository; // Внедряем репозиторий

    @GetMapping("/hello")
    public String hello(
            @RequestHeader(value = "X-User-Name", defaultValue = "Guest") String username, 
            Model model) {
        
        model.addAttribute("username", username);
        model.addAttribute("firstLetter", username.substring(0, 1).toUpperCase());
        
        // Получаем все картинки из базы photo_db
        model.addAttribute("pictures", resourseRepository.findAll());
        
        return "hello";
    }
}