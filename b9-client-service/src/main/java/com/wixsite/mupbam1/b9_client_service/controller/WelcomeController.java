package com.wixsite.mupbam1.b9_client_service.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WelcomeController {

    // Упрощаем: просто отдаем страницу. Вся магия будет в JS.
    @GetMapping({"/", ""}) 
    public String welcomePage() {
        return "welcome";
    }
    @GetMapping("/data")
    public String dashboardPage() {
        return "dashboard"; // Отдает templates/dashboard.html
    }
}