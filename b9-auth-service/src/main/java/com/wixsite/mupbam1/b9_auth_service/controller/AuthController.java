package com.wixsite.mupbam1.b9_auth_service.controller;

import com.wixsite.mupbam1.b9_auth_service.service.ResourseService;
import com.wixsite.mupbam1.b9_auth_service.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    private final ResourseService authService;

    public AuthController(JwtService jwtService, ResourseService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    // Добавляем этот метод, чтобы страница открывалась по GET запросу
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // вернет register.html из templates
    }

    @GetMapping("/login")
    public String loginPage() { 
        return "login"; 
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpServletResponse response) {
        String token = authService.login(username, password);
        
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");  
        response.addCookie(cookie);
        
        return "redirect:/hello";
    }

    @PostMapping("/register")
    // Убрали @ResponseBody, чтобы сделать редирект на логин после регистрации
    public String register(@RequestParam String username, @RequestParam String password) {
        authService.register(username, password);
        return "redirect:/auth/login"; // После регистрации отправляем на вход
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
    
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String handleRuntimeException(RuntimeException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return "Error: " + e.getMessage();
    }
}