package com.wixsite.mupbam1.b9_auth_service.controller;

import com.wixsite.mupbam1.b9_auth_service.service.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/token")
    public String getToken(@RequestParam String name) {
        // В будущем здесь будет проверка пароля, а пока просто выпускаем токен
        return jwtService.generateToken(name);
    }
}