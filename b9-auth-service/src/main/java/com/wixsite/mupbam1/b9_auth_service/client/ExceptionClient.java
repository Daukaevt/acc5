package com.wixsite.mupbam1.b9_auth_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.wixsite.mupbam1.b9_auth_service.models.ErrorLog;

@Component
public class ExceptionClient {
    private final RestTemplate restTemplate = new RestTemplate();
    
    // Используем имя контейнера из твоего compose
    private final String URL = "http://b9-exception-service:8085/exceptions/log";

    public void sendError(ErrorLog log) {
        try {
            // У рекорда нет сеттеров, поэтому объект 'log' уже должен 
            // содержать timestamp, созданный в GlobalExceptionHandler
            restTemplate.postForEntity(URL, log, String.class);
            System.out.println("✅ Log sent to Exception Service");
        } catch (Exception e) {
            System.err.println("❌ CRITICAL: Exception Service is down! " + e.getMessage());
        }
    }
}