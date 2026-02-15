package com.wixsite.mupbam1.b9_client_service.controller;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class ClientDataController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/data")
    public Map<String, Object> getClientData(HttpServletRequest request) {
        log.info("=== Входящий запрос на /client/data ===");

        String username = request.getHeader("X-User-Name");
        
        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("status", username != null ? "authenticated" : "anonymous");
        responseData.put("username", username != null ? username : "Guest");

        if (username != null) {
            try {
                // 1. Извлекаем JWT из куки входящего запроса
                String jwtCookie = null;
                if (request.getCookies() != null) {
                    for (var cookie : request.getCookies()) {
                        if ("jwt".equals(cookie.getName())) {
                            jwtCookie = cookie.getValue();
                            break;
                        }
                    }
                }

                if (jwtCookie != null) {
                    // 2. Формируем заголовки для запроса к Gateway
                    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                    headers.add("Cookie", "jwt=" + jwtCookie);
                    
                    var entity = new org.springframework.http.HttpEntity<>(headers);

                    log.info("Запрашиваю данные у Gateway с токеном...");
                    String resourceUrl = "http://api-gateway:8080/hello/api/users/my";
                    
                    // Используем exchange, чтобы передать заголовки
                    var response = restTemplate.exchange(
                        resourceUrl, 
                        org.springframework.http.HttpMethod.GET, 
                        entity, 
                        Object.class
                    );

                    responseData.put("resource_data", response.getBody());
                    log.info("Данные успешно получены через Relay Token");
                } else {
                    log.warn("JWT кука не найдена в запросе!");
                    responseData.put("resource_error", "Missing JWT token");
                }
                
            } catch (Exception e) {
                log.error("Ошибка при запросе к 8080: {}", e.getMessage());
                responseData.put("resource_error", "Ошибка авторизации или связи: " + e.getMessage());
            }
        }

        responseData.put("timestamp", LocalDateTime.now().toString());
        return responseData;
    }
}