package com.wixsite.mupbam1.b9_client_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WelcomeController {

    // Добавляем пустой путь "", чтобы поймать запрос без слэша
    @GetMapping({"/", ""}) 
    public String welcomePage(Model model, HttpServletRequest request) {
        String username = request.getHeader("X-User-Name");
        boolean isAuthenticated = (username != null && !username.isEmpty());

        model.addAttribute("authenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? username : "Гость");
        
        return "welcome";
    }
}