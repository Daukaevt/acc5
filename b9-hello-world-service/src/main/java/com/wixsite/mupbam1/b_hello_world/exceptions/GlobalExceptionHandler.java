package com.wixsite.mupbam1.b_hello_world.exceptions;

import com.wixsite.mupbam1.b_hello_world.models.ErrorLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Arrays;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
    // Используем имя контейнера из docker-compose
    private final RestClient restClient = RestClient.create("http://b9-exception-service:8085");

    // 1. СПЕЦИАЛЬНЫЙ ОБРАБОТЧИК ДЛЯ 404 (Не замусоривает БД)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(Exception ex) {
        System.out.println("⚠ User error (404): " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 2. ОБЩИЙ ОБРАБОТЧИК (Для реальных багов 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLog> handleAndReport(Exception ex) {
        
        // Создаем отчет с использованием компактного StackTrace
        ErrorLog report = new ErrorLog(
            "b9-hello-service", 
            ex.getMessage(), 
            getShortStackTrace(ex), // Применяем обрезку здесь
            LocalDateTime.now()      // Синхронизируем с LocalDateTime, как в других сервисах
        );
        
        try {
            restClient.post()
                      .uri("/exceptions/log") // Сопоставили с контроллером b9-exception-service
                      .body(report)
                      .retrieve()
                      .toBodilessEntity();
            System.out.println("✅ Critical error sent to exception-service");
        } catch (Exception e) {
            System.err.println("❌ Failed to send log: " + e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(report); 
    }

    // Тот самый метод оптимизации логов
    private String getShortStackTrace(Exception ex) {
        StackTraceElement[] elements = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        
        int limit = Math.min(elements.length, 10);
        for (int i = 0; i < limit; i++) {
            sb.append(elements[i].toString()).append("\n");
        }
        
        String trace = sb.toString();
        return trace.length() > 2000 ? trace.substring(0, 2000) : trace;
    }
}