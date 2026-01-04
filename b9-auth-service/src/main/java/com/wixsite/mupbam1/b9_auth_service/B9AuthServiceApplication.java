package com.wixsite.mupbam1.b9_auth_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class B9AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(B9AuthServiceApplication.class, args);
    }
}

@Component
class VaultChecker implements CommandLineRunner {

    // Спринг сам подставит сюда значение из Vault при старте
    @Value("${jwt.secret:КЛЮЧ_НЕ_НАЙДЕН}")
    private String jwtSecret;

    @Override
    public void run(String... args) {
        System.out.println("------------------------------------");
        System.out.println("Ключ из Vault: " + jwtSecret);
        System.out.println("------------------------------------");
    }
}