package com.wixsite.mupbam1.b_hello_world.exceptions;

import com.wixsite.mupbam1.b_hello_world.models.ErrorLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
    private final RestClient restClient = RestClient.create("http://exception-service:8085");

    // 1. СПЕЦИАЛЬНЫЙ ОБРАБОТЧИК ДЛЯ 404 (Не замусоривает БД)
    @ExceptionHandler(ResourceNotFoundException.class) // Создай этот класс или используй ResponseStatusException
    public ResponseEntity<String> handleNotFound(Exception ex) {
        System.out.println("⚠ User error (404): " + ex.getMessage());
        // Мы НЕ отправляем это в restClient. Просто отдаем ответ.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 2. ОБЩИЙ ОБРАБОТЧИК (Для реальных багов 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLog> handleAndReport(Exception ex) {
        
        ErrorLog report = new ErrorLog(
            "hello-service", 
            ex.getMessage(), 
            Arrays.toString(ex.getStackTrace()),
            Instant.now()
        );
        
        // Отправляем в базу ТОЛЬКО критические ошибки (те, что попали сюда)
        try {
            restClient.post()
                      .uri("/log")
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
}