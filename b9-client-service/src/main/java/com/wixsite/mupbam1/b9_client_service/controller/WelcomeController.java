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
    /*
    @GetMapping("/data")
    public String getClientDataPage(HttpServletRequest request, Model model) {
        String username = request.getHeader("X-User-Name");
        
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", username != null ? "authenticated" : "anonymous");
        data.put("username", username != null ? username : "Guest");
        data.put("timestamp", LocalDateTime.now().toString());
        data.put("service", "b9-client-service");

        // Передаем объект как Map, Thymeleaf сам может работать с ним, 
        // но для вывода "как в JSON" мы передадим его в модель
        model.addAttribute("json_data", data);
        
        return "data_view"; // Имя нашего нового HTML файла
    }
    */
 // Новый эндпоинт прямо на 8086
    @GetMapping("/api/profile")
    @ResponseBody // Указывает, что возвращаем JSON, а не HTML-шаблон
    public Map<String, String> getUserProfile(HttpServletRequest request) {
        // Вытаскиваем имя, которое Гейтвей прокинул в заголовке
        String username = request.getHeader("X-User-Name");
        
        Map<String, String> data = new HashMap<>();
        if (username != null && !username.isEmpty()) {
            data.put("username", username);
            data.put("status", "authenticated");
        } else {
            data.put("username", "Guest");
            data.put("status", "anonymous");
        }
        return data; 
    }
}