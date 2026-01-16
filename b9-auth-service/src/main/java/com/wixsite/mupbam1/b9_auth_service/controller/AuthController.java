package com.wixsite.mupbam1.b9_auth_service.controller;

import com.wixsite.mupbam1.b9_auth_service.service.AuthService;
import com.wixsite.mupbam1.b9_auth_service.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    // Показывает страницу логина (нужен login.html в templates)
    @GetMapping("/login")
    public String loginPage() { 
        return "login"; 
    }

    // ОБЯЗАТЕЛЬНО: Аннотация PostMapping для обработки данных формы или curl
    @PostMapping("/login")
    @ResponseBody // Добавь это, если хочешь вернуть токен текстом, а не делать редирект
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        String token = authService.login(username, password);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return token; // Для теста возвращаем сам токен
    }

    // НОВОЕ: Добавляем регистрацию
    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestParam String username, @RequestParam String password) {
        return authService.register(username, password);
    }

    @GetMapping("/test")
    @ResponseBody
    public String testGet() {
        return "Контроллер виден, GET метод работает!";
    }

    @GetMapping("/token")
    @ResponseBody
    public String getToken(@RequestParam String name) {
        return jwtService.generateToken(name);
    }
}