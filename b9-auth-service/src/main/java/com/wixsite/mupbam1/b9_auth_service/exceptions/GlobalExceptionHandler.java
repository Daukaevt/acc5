package com.wixsite.mupbam1.b9_auth_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import com.wixsite.mupbam1.b9_auth_service.models.ErrorLog;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
    private final RestClient restClient = RestClient.create();
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLog> handleAndReport(Exception ex, HttpServletRequest request) {
        // Создаем отчет через конструктор Record (без Builder)
        // Убираем methodName, так как его нет в общей модели
        ErrorLog report = new ErrorLog(
                "b9-auth-service",
                ex.getMessage(),
                getShortStackTrace(ex),
                LocalDateTime.now()
        );
        
        try {
            restClient.post()
                      .uri("http://b9-exception-service:8085/exceptions/log")
                      .body(report)
                      .retrieve()
                      .toBodilessEntity();
            System.out.println("✅ Auth error sent to exception-service");
        } catch (Exception e) {
            System.err.println("❌ Failed to log exception: " + e.getMessage());
        }

        // Возвращаем ResponseEntity, чтобы клиент тоже видел, что упало
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(report);
    }

    // Метод для фильтрации и обрезки StackTrace
    private String getShortStackTrace(Exception ex) {
        StackTraceElement[] elements = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        
        // Берем только первые 10 элементов (самая суть ошибки)
        int limit = Math.min(elements.length, 10);
        for (int i = 0; i < limit; i++) {
            sb.append(elements[i].toString()).append("\n");
        }
        
        String trace = sb.toString();
        // Жесткое ограничение по символам для БД
        return trace.length() > 2000 ? trace.substring(0, 2000) : trace;
    }
}